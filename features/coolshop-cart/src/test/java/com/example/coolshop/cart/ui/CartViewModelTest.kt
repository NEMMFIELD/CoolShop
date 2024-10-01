package com.example.coolshop.cart.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.coolshop.cart.domain.ProductObserveUseCase
import com.example.coolshop.cart.domain.RemoveItemFromCartUseCase
import com.example.coolshop.cart.domain.TotalPriceUseCase
import com.example.database.models.CoolShopDBO
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals


class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CartViewModel
    private val removeItem: RemoveItemFromCartUseCase = mockk()
    private val observedProductObserveUseCase: ProductObserveUseCase = mockk()
    private val totalPriceUseCase: TotalPriceUseCase = mockk()
    private val observerProduct: Observer<List<CoolShopDBO>> = mockk(relaxed = true)
    private val observerPrice: Observer<Double?> = mockk(relaxed = true)

    @Before
    fun setUp() {
        // Инициализация ViewModel с замокированными зависимостями
        viewModel = CartViewModel(removeItem, observedProductObserveUseCase, totalPriceUseCase)

        // Присоединение наблюдателей к LiveData
        viewModel.cartedProducts.observeForever(observerProduct)
        viewModel.totalPrice.observeForever(observerPrice)

        // Настройка мока для observedProduct
        every { observedProductObserveUseCase.observedProduct } returns flowOf(emptyList())
    }

    @Test
    fun `removeItem should call removeItem use case`() = runTest {
        // Arrange
        val productToRemove = CoolShopDBO(
            id = 1,
            title = "Product 1",
            price = 10.0,
            category = "electronics",
            description = "Description",
            isLiked = false,
            rate = 4.5,
            imgPath = "vkontakte/img"
        )

        // Act
        viewModel.removeItem(productToRemove)

        // Assert
        coVerify { removeItem.execute(productToRemove) }
    }

    @Test
    fun `cartedProducts LiveData should update correctly`() {
        // Arrange
        val expectedProducts = listOf(
            CoolShopDBO(
                id = 1,
                title = "Product 1",
                price = 10.0,
                category = "electronics",
                description = "Description",
                isLiked = false,
                rate = 4.5,
                imgPath = "vkontakte/img"
            )
        )
        every { observedProductObserveUseCase.observedProduct } returns flowOf(expectedProducts)

        // Act
        viewModel.cartedProducts.observeForever(observerProduct)

        // Assert
        assertEquals(expectedProducts, viewModel.cartedProducts.value)
        verify { observerProduct.onChanged(expectedProducts) }
    }

    @Test
    fun `totalPrice LiveData should update correctly`() {
        // Arrange
        val expectedPrice: Double? = 100.0 // Убедитесь, что здесь используется правильный тип
        every { totalPriceUseCase.observedTotalPrice } returns flowOf(expectedPrice)

        // Act
        viewModel.totalPrice.observeForever(observerPrice)

        // Assert
        assertEquals(expectedPrice, viewModel.totalPrice.value)
        verify { observerPrice.onChanged(expectedPrice) }
    }

    @After
    fun tearDown() {
        // Удаление наблюдателей
        viewModel.cartedProducts.removeObserver(observerProduct)
        viewModel.totalPrice.removeObserver(observerPrice)
    }
}
