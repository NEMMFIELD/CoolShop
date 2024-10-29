package com.example.coolshop.cart.ui

import com.example.coolshop.cart.domain.ProductObserveUseCase
import com.example.coolshop.cart.domain.RemoveItemFromCartUseCase
import com.example.coolshop.cart.domain.TotalPriceUseCase
import com.example.database.models.CoolShopDBO
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class CartViewModelTest {
    // Mock-объекты для зависимостей
    private val removeItemUseCase: RemoveItemFromCartUseCase = mockk(relaxed = true)
    private val productObserveUseCase: ProductObserveUseCase = mockk()
    private val totalPriceUseCase: TotalPriceUseCase = mockk()

    // Тестируемая ViewModel
    private lateinit var viewModel: CartViewModel

    // Подготовка перед каждым тестом
    @Before
    fun setup() {
        // Используем TestCoroutineDispatcher и TestCoroutineScope для корутин
        Dispatchers.setMain(StandardTestDispatcher())

        // Инициализация mock для StateFlow
        every { productObserveUseCase.observedProductsInCart } returns MutableStateFlow(emptyList())
        every { totalPriceUseCase.observedTotalPriceInCart } returns MutableStateFlow(null)

        // Инициализация viewModel с mock-объектами
        viewModel = CartViewModel(removeItemUseCase, productObserveUseCase, totalPriceUseCase)
    }

    @Test
    fun `should observe products in cart`() = runTest {
        val testProducts = listOf(CoolShopDBO(1, "Product1", "path", 10.0, 5.0, true, "Description", "Category"))
        val productsFlow = MutableStateFlow(emptyList<CoolShopDBO>()) // Изначально пусто

        // Настройка mock для потока продуктов в корзине
        every { productObserveUseCase.observedProductsInCart } returns productsFlow

        // Инициализация ViewModel
        viewModel = CartViewModel(removeItemUseCase, productObserveUseCase, totalPriceUseCase)

        // Запуск корутины для сбора данных из StateFlow
        val collectedProducts = mutableListOf<List<CoolShopDBO>>()
        val job = launch {
            viewModel.productsInCart?.collect { collectedProducts.add(it) }
        }

        // Эмитируем тестовые данные в поток
        productsFlow.value = testProducts

        // Симулируем задержку, чтобы дождаться эмиссии данных
        advanceTimeBy(1000)

        // Проверяем, что собранные данные совпадают с тестовыми
        assertEquals(testProducts, collectedProducts.last())

        job.cancel() // Завершаем корутину
    }

    @Test
    fun `should observe total price in cart`() = runTest {
        // Подготовка тестовых данных
        val testTotalPrice = 100.0
        val totalPriceFlow = MutableStateFlow<Double?>(null) // Изначально null

        // Настройка mock для потока общей стоимости
        every { totalPriceUseCase.observedTotalPriceInCart } returns totalPriceFlow

        // Инициализация ViewModel с обновленным totalPriceUseCase
        viewModel = CartViewModel(removeItemUseCase, productObserveUseCase, totalPriceUseCase)

        // Запуск корутины для сбора данных из StateFlow
        val collectedPrices = mutableListOf<Double?>()
        val job = launch {
            viewModel.totalPrice?.collect { collectedPrices.add(it) }
        }

        // Эмитируем тестовую общую стоимость в поток
        totalPriceFlow.value = testTotalPrice

        // Симулируем задержку, чтобы дождаться эмиссии данных
        advanceTimeBy(1000)

        // Проверяем, что собранная общая стоимость совпадает с тестовой
        assertEquals(testTotalPrice, collectedPrices.last())

        job.cancel() // Завершаем корутину после теста
    }

    @Test
    fun `should remove item from cart`() = runTest {
        val testProduct = CoolShopDBO(1, "Product1", "path", 10.0, 5.0, true, "Description", "Category")

        // Настройка mock для RemoveItemFromCartUseCase
        coEvery { removeItemUseCase.execute(testProduct) } just Runs // Ожидаем, что метод будет вызван

        // Инициализация ViewModel
        viewModel = CartViewModel(removeItemUseCase, productObserveUseCase, totalPriceUseCase)

        // Вызываем метод удаления
        viewModel.removeItem(testProduct)

        // Даем время завершиться корутине
        advanceUntilIdle() // Это важно, чтобы корутина завершила свою работу

        // Проверяем, что метод execute был вызван с ожидаемым объектом
        coVerify { removeItemUseCase.execute(testProduct) }
    }
}
