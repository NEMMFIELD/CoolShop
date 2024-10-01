package com.example.coolshop.user.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.user.domain.LoginUseCase
import com.example.coolshop.user.domain.SaveTokenUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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

    private lateinit var viewModel: UserViewModel
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var saveTokenUseCase: SaveTokenUseCase

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Mock LoginUseCase and SaveTokenUseCase
        loginUseCase = mockk()
        saveTokenUseCase = mockk()

        // Initialize the ViewModel
        viewModel = UserViewModel(loginUseCase, saveTokenUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset to avoid affecting other tests
        unmockkAll() // Unmock all mocks
    }

    @Test
    fun `login should set token when login is successful`() = runTest {
        // Mock a successful login response
        val loginRequest = LoginRequest("username", "password")
        val expectedToken = "test_token"
        coEvery { loginUseCase.execute(loginRequest) } returns RegistrationResponse(expectedToken)

        // Call login
        viewModel.login(loginRequest)

        // Advance time to ensure coroutines complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert the token is set correctly
        assertEquals(expectedToken, viewModel.token.getOrAwaitValue())
    }

    @Test
    fun `login should handle exception when login fails`() = runBlockingTest {
        // Arrange
        val loginRequest = LoginRequest("username", "password")
        val exception = Exception("Login failed")

        // Mock the use case to throw an exception
        coEvery { loginUseCase.execute(loginRequest) } throws exception

        // Mock Log.d using MockK
        mockkStatic(Log::class)

        // Act
        viewModel.login(loginRequest)

        // Assert
        // Проверка, что Log.d был вызван с ожидаемыми параметрами
        verify { Log.d("Error", "Error from login: $exception") }

        // Проверка, что токен остается пустым в случае ошибки
        assertNull(viewModel.token.value)

        // Close the static mock to prevent memory leaks
        unmockkStatic(Log::class)
    }

    @Test
    fun `saveToken should call saveTokenUseCase`() = runTest {
        // Define token to save
        val tokenToSave = "test_token"

        // Call saveToken
        viewModel.saveToken(tokenToSave)

        // Advance time to ensure coroutines complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that saveTokenUseCase was called with the correct token
        coVerify { saveTokenUseCase.execute(tokenToSave) }
    }

    @Test
    fun `saveToken should handle exception when saving token fails`() = runTest {
        // Mock an exception from saveTokenUseCase
        val tokenToSave = "test_token"
        val exception = Exception("Save token failed")
        coEvery { saveTokenUseCase.execute(tokenToSave) } throws exception

        // Call saveToken
        viewModel.saveToken(tokenToSave)

        // Advance time to ensure coroutines complete
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify that saveTokenUseCase was called
        coVerify { saveTokenUseCase.execute(tokenToSave) }
        // Optionally verify that the error was logged (requires additional setup to capture logs)
    }

    fun <T> LiveData<T>.getOrAwaitValue(): T {
        var data: T? = null
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(value: T) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }

        this.observeForever(observer)

        // Wait to get the value.
        latch.await(2, TimeUnit.SECONDS)

        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}
