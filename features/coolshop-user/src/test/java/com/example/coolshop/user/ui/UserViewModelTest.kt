package com.example.coolshop.user.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
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


@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val loginUseCase: LoginUseCase = mockk()
    private val saveTokenUseCase: SaveTokenUseCase = mockk()
    private val logger: Logger = mockk(relaxed = true)
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
    private lateinit var viewModel: UserViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(loginUseCase, saveTokenUseCase, logger, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login should update token and call saveToken`() = runTest {
        // Arrange
        val loginRequest = LoginRequest("username", "password")
        val expectedToken = "test_token"
        coEvery { loginUseCase.execute(loginRequest) } returns RegistrationResponse(expectedToken)
        coEvery { saveTokenUseCase.execute(expectedToken) } just Runs

        // Act
        viewModel.login(loginRequest)

        // Assert
        assertEquals(expectedToken, viewModel.token.first())
        assertEquals(expectedToken, savedStateHandle["token"])
        coVerify { saveTokenUseCase.execute(expectedToken) }
    }

    @Test
    fun `login should log exception on failure`() = runTest {
        // Arrange
        val loginRequest = LoginRequest("username", "password")
        val exception = RuntimeException("Network error")
        coEvery { loginUseCase.execute(loginRequest) } throws exception
        every { logger.d(any(), any()) } just Runs

        // Act
        viewModel.login(loginRequest)

        // Assert
        coVerify { logger.d("Error", "Coroutine exception: $exception") }
    }
}
