package com.example.coolshop.user.domain

import com.example.coolshop.user.data.LoginRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class SaveTokenUseCaseTest {
    private lateinit var repository: LoginRepository
    private lateinit var useCase: SaveTokenUseCase

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        useCase = SaveTokenUseCase(repository)
    }

    @Test
    fun `execute should save token`() = runBlocking {
        val token = "myToken"
        useCase.execute(token)
        coVerify { repository.saveToken(token) }
    }
}

