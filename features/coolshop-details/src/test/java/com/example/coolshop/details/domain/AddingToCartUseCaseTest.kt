package com.example.coolshop.details.domain

import com.example.database.models.CoolShopDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test


class AddingToCartUseCaseTest {
    private lateinit var repository: CoolShopDetailsRepository
    private lateinit var useCase: AddingToCartUseCase

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk()
        // Инициализируем use case с мокнутым репозиторием
        useCase = AddingToCartUseCase(repository)
    }

    @Test
    fun `execute should call addToCart with correct product`() = runTest {
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

        // Настройка мока для метода addToCart
        coEvery { repository.addToCart(product) } returns Unit // Здесь ничего не возвращается, но мы указываем, что метод будет вызван

        // When
        useCase.execute(product)

        // Then
        // Проверяем, что метод addToCart был вызван один раз с правильным продуктом
        coVerify(exactly = 1) { repository.addToCart(product) }
    }
}
