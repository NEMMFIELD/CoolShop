package com.example.coolshop.cart.domain

import com.example.database.models.CoolShopDBO
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test


class ProductObserveUseCaseTest {
    private lateinit var repository: CartRepository
    private lateinit var productObserveUseCase: ProductObserveUseCase

    @Before
    fun setUp() {
        // Создаем мок репозитория
        repository = mockk(relaxed = true)
        // Инициализируем UseCase с мокнутым репозиторием
        productObserveUseCase = ProductObserveUseCase(repository)
    }

    @Test
    fun `observedProduct should return the correct list of products from repository`() = runTest {
        // Given: список продуктов, который будет возвращен из flowCart
        val expectedProducts = listOf(
            CoolShopDBO(id = 1, title = "Product 1", price = 10.0, category = "electronics", description = "Description", isLiked = false, rate = 4.5, imgPath = "vkontakte/img"),
            CoolShopDBO(id = 2, title = "Product 2", price = 20.0,category = "electronics",description = "Description", isLiked = false, rate = 5.5, imgPath = "facebook/img"  )
        )
        every { repository.flowCart } returns flowOf(expectedProducts)

        // When: получаем поток с продуктами из use case
        val resultFlow = productObserveUseCase.observedProduct

        // Then: collect используем внутри runTest и проверяем результат
        resultFlow.collect { productList ->
            assertEquals(expectedProducts, productList)
        }

        // Проверяем, что вызов flowCart был сделан
        verify { repository.flowCart }
    }
}
