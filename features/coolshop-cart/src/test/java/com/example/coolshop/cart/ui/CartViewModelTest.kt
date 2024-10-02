package com.example.coolshop.cart.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.coolshop.cart.domain.ProductObserveUseCase
import com.example.coolshop.cart.domain.RemoveItemFromCartUseCase
import com.example.coolshop.cart.domain.TotalPriceUseCase
import com.example.database.models.CoolShopDBO
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals


class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var removeItemFromCartUseCase: RemoveItemFromCartUseCase

    @MockK
    private lateinit var productObserveUseCase: ProductObserveUseCase

    @MockK
    private lateinit var totalPriceUseCase: TotalPriceUseCase

    private lateinit var viewModel: CartViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)

        // Замокируем потоки, возвращаемые useCase
        every { productObserveUseCase.observedProduct } returns flowOf(listOf(CoolShopDBO(1, "Product", "", 100.0, 4.5, false, "Test product", "Category")))
        every { totalPriceUseCase.observedTotalPrice } returns flowOf(100.0)

        // Создаем ViewModel
        viewModel = CartViewModel(removeItemFromCartUseCase, productObserveUseCase, totalPriceUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cartedProducts should observe products from ProductObserveUseCase`() = runTest {
        // Given
        val observer = mockk<Observer<List<CoolShopDBO>>>(relaxed = true)
        viewModel.cartedProducts.observeForever(observer)

        // When
        val slot = slot<List<CoolShopDBO>>()
        val expectedProducts = listOf(CoolShopDBO(1, "Product", "", 100.0, 4.5, false, "Test product", "Category"))

        // Then
        verify { observer.onChanged(capture(slot)) }

        // Compare objects by fields instead of reference
        assertEquals(expectedProducts.size, slot.captured.size)
        assertEquals(expectedProducts[0].id, slot.captured[0].id)
        assertEquals(expectedProducts[0].title, slot.captured[0].title)
        assertEquals(expectedProducts[0].price, slot.captured[0].price)

        viewModel.cartedProducts.removeObserver(observer)
    }

    @Test
    fun `totalPrice should observe total price from TotalPriceUseCase`() = runTest {
        // Given
        val observer = mockk<Observer<Double?>>(relaxed = true)
        viewModel.totalPrice.observeForever(observer)

        // When
        val expectedPrice = 100.0

        // Then
        verify { observer.onChanged(expectedPrice) }
        assertEquals(expectedPrice, viewModel.totalPrice.value)

        viewModel.totalPrice.removeObserver(observer)
    }

    @Test
    fun `removeItem should call removeItemFromCartUseCase`() = runTest {
        // Given
        val product = CoolShopDBO(1, "Product", "", 100.0, 4.5, false, "Test product", "Category")
        coEvery { removeItemFromCartUseCase.execute(product) } just Runs

        // When
        viewModel.removeItem(product)

        // Then
        coVerify { removeItemFromCartUseCase.execute(product) }
    }
}
