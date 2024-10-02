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
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var saveTokenUseCase: SaveTokenUseCase

    @MockK
    private lateinit var logger: Logger

    private lateinit var userViewModel: UserViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        userViewModel = UserViewModel(loginUseCase, saveTokenUseCase, logger)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should update token on success`() = runTest {
        // Given
        val loginRequest = LoginRequest("user", "password")
        val expectedToken = "testToken"
        coEvery { loginUseCase.execute(loginRequest) } returns RegistrationResponse(expectedToken)

        val observer = mockk<Observer<String>>(relaxed = true)
        userViewModel.token.observeForever(observer)

        // When
        userViewModel.login(loginRequest)

        // Then
        coVerify { loginUseCase.execute(loginRequest) }
        assertEquals(expectedToken, userViewModel.token.value)
        verify { observer.onChanged(expectedToken) }

        userViewModel.token.removeObserver(observer)
    }

    @Test
    fun `login should log error on exception`() = runTest {
        // Given
        val loginRequest = LoginRequest("user", "password")
        val exception = Exception("Login error")
        coEvery { loginUseCase.execute(loginRequest) } throws exception

        // When
        userViewModel.login(loginRequest)

        // Then
        coVerify { loginUseCase.execute(loginRequest) }
        verify { logger.d("Error", "Error from login:${exception}") }
    }

    @Test
    fun `saveToken should call saveTokenUseCase on success`() = runTest {
        // Given
        val token = "testToken"
        coEvery { saveTokenUseCase.execute(token) } just Runs

        // When
        userViewModel.saveToken(token)

        // Then
        coVerify { saveTokenUseCase.execute(token) }
    }

    @Test
    fun `saveToken should log error on exception`() = runTest {
        // Given
        val token = "testToken"
        val exception = Exception("Save token error")
        coEvery { saveTokenUseCase.execute(token) } throws exception

        // When
        userViewModel.saveToken(token)

        // Then
        coVerify { saveTokenUseCase.execute(token) }
        verify { logger.d("Error", "Error from save token:${exception}") }
    }
}
