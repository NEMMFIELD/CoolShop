package com.example.coolshop.user.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.user.domain.LoginUseCase
import com.example.coolshop.user.domain.SaveTokenUseCase
import com.example.utils.Logger
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class UserViewModelTest {

    private lateinit var viewModel: UserViewModel
    private val loginUseCase = mockk<LoginUseCase>()
    private val saveTokenUseCase = mockk<SaveTokenUseCase>()
    private val logger = mockk<Logger>(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = UserViewModel(loginUseCase, saveTokenUseCase, logger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should update token on successful login`() = runTest {
        // Подготовка тестового запроса
        val loginRequest = LoginRequest("username", "password")
        val expectedToken = "test_token"

        // Настройка мока для LoginUseCase
        coEvery { loginUseCase.execute(loginRequest) } returns RegistrationResponse(expectedToken)

        // Запуск корутины для сбора состояния токена
        val job = launch {
            viewModel.token.first()
        }

        // Выполнение входа
        viewModel.login(loginRequest)

        // Проверяем, что токен обновился
        advanceUntilIdle() // Дожидаемся выполнения всех корутин
        assertEquals(expectedToken, viewModel.token.value)

        job.cancel() // Завершаем корутину
    }

    @Test
    fun `should call saveTokenUseCase when saving token`() = runTest {
        // Подготовка тестового токена
        val tokenToSave = "test_token"

        // Ожидание вызова execute в saveTokenUseCase
        coEvery { saveTokenUseCase.execute(tokenToSave) } just Runs

        // Выполнение сохранения токена
        viewModel.saveToken(tokenToSave)

        // Даем время для завершения корутины
        advanceUntilIdle()

        // Проверяем, что метод saveTokenUseCase был вызван
        coVerify { saveTokenUseCase.execute(tokenToSave) }
    }
}
