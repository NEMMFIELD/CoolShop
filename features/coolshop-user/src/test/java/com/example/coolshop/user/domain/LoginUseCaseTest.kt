package com.example.coolshop.user.domain

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class LoginUseCaseTest {
    private lateinit var repository: LoginRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk()
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun `execute should return correct registration response`() = runBlocking {
        // Given: параметры запроса и ожидаемый ответ
        val loginRequest = LoginRequest(username = "test", password = "password")
        val expectedResponse = RegistrationResponse("token")

        // Мокируем вызов метода login в репозитории
        coEvery { repository.login(loginRequest) } returns expectedResponse

        // When: вызываем execute в use case
        val actualResponse = loginUseCase.execute(loginRequest)

        // Then: проверяем, что результат соответствует ожидаемому
        assertEquals(expectedResponse, actualResponse)

        // Verify: проверяем, что вызов repository.login() был сделан с правильным аргументом
        coVerify { repository.login(loginRequest) }
    }
}
