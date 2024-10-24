package com.example.coolshop.cart.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals


class TotalPriceUseCaseTest {

    private lateinit var repository: CartRepository
    private lateinit var totalPriceUseCase: TotalPriceUseCase

    @Before
    fun setUp() {
        // Создаем мок для репозитория
        repository = mockk(relaxed = true)
        // Инициализируем UseCase с мокнутым репозиторием
        totalPriceUseCase = TotalPriceUseCase(repository)
    }

    @Test
    fun `observedTotalPrice should return the correct total price from repository`() = runTest {
        // Given: ожидаемая сумма, которую будет возвращать flowSum
        val expectedTotalPrice = 100.0
        every { repository.flowSum } returns flowOf(expectedTotalPrice)

        // When: получаем flow с итоговой суммой из use case
        val resultFlow = totalPriceUseCase.observedTotalPriceInCart

        // Then: collect используем внутри runTest и проверяем результат
        resultFlow.collect { totalPrice ->
            assertEquals(expectedTotalPrice, totalPrice!!, 0.0)
        }

        // Проверяем, что вызов был сделан
        verify { repository.flowSum }
    }
}
