package com.example.coolshop.cart.domain

import com.example.data.CoolShopModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class LoadCartUseCaseTest {
    private lateinit var repository: CartRepository
    private lateinit var useCase: LoadCartUseCase

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk()
        // Инициализируем use case с мокнутым репозиторием
        useCase = LoadCartUseCase(repository)
    }

    @Test
    fun `execute should return list of CoolShopModel from repository`() = runTest {
        // Given
        val expectedProducts = listOf(
            CoolShopModel(
                id = 1,
                title = "Product 1",
                imgPath = "/img1.jpg",
                price = 100.0,
                rate = 4.5,
                isLiked = false,
                description = "Product 1 description",
                category = "Category 1"
            ),
            CoolShopModel(
                id = 2,
                title = "Product 2",
                imgPath = "/img2.jpg",
                price = 200.0,
                rate = 4.0,
                isLiked = true,
                description = "Product 2 description",
                category = "Category 2"
            )
        )

        // Настройка мока для метода loadProductsFromDatabase
        coEvery { repository.loadProductsFromDatabase() } returns expectedProducts

        // When
        val result = useCase.execute()

        // Collect the results from the flow
        val actualProducts = result.first() // Используем first() для получения списка из Flow

        // Then
        assertEquals(expectedProducts, actualProducts)
        coVerify(exactly = 1) { repository.loadProductsFromDatabase() } // Проверяем, что метод был вызван один раз
    }
}
