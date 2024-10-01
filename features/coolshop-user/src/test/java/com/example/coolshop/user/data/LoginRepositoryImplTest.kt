package com.example.coolshop.user.data

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class LoginRepositoryImplTest {
    private lateinit var api: CoolShopApi
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var loginRepository: LoginRepositoryImpl

    @Before
    fun setUp() {
        // Создаем моки для API и SharedPreferences
        api = mockk()
        sharedPreferences = mockk(relaxed = true)
        loginRepository = LoginRepositoryImpl(api, sharedPreferences)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `login should return correct RegistrationResponse`() = runBlocking {
        // Given: запрос на логин и ожидаемый ответ
        val loginRequest = LoginRequest(username = "test", password = "password")
        val expectedResponse = RegistrationResponse(token = "test_token")

        // Мокируем вызов API
        coEvery { api.login(loginRequest) } returns expectedResponse

        // When: вызываем метод login в репозитории
        val actualResponse = loginRepository.login(loginRequest)

        // Then: проверяем, что результат соответствует ожидаемому
        assertEquals(expectedResponse, actualResponse)

        // Verify: проверяем, что вызов API был сделан с правильным запросом
        coVerify { api.login(loginRequest) }
    }

    @Test
    fun `saveToken should save token in SharedPreferences`() = runBlocking {
        // Given: токен, который нужно сохранить
        val token = "test_token"
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)

        // Мокируем методы SharedPreferences
        every { sharedPreferences.edit() } returns editor
        every { editor.putString("token", token) } returns editor

        // When: вызываем метод saveToken в репозитории
        loginRepository.saveToken(token)

        // Then: проверяем, что токен был сохранен в SharedPreferences
        verify {
            sharedPreferences.edit()
            editor.putString("token", token)
            editor.apply()
        }
    }
}
