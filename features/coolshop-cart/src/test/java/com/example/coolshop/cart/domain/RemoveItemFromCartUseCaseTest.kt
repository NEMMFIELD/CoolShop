package com.example.coolshop.cart.domain

import com.example.database.models.CoolShopDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test


class RemoveItemFromCartUseCaseTest {
    private lateinit var repository: CartRepository
    private lateinit var useCase: RemoveItemFromCartUseCase

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk()
        // Инициализируем use case с мокнутым репозиторием
        useCase = RemoveItemFromCartUseCase(repository)
    }

    @Test
    fun `execute should call repository removeProductFromCart method`() = runTest {
        // Given
        val product = CoolShopDBO(
            id = 1,
            title = "Product 1",
            imgPath = "/img1.jpg",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Product 1 description",
            category = "Category 1"
        )

        // Настройка мока для метода removeProductFromCart
        coEvery { repository.removeProductFromCart(product) } returns Unit // Мокируем возвращаемое значение

        // When
        useCase.execute(product)

        // Then
        coVerify(exactly = 1) { repository.removeProductFromCart(product) } // Проверяем, что метод был вызван один раз
    }

}
