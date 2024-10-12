package com.example.coolshop.details.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.coolshop.details.domain.AddingToCartUseCase
import com.example.coolshop.details.domain.CoolShopDetailsUseCase
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class CoolShopDetailsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @RelaxedMockK
    private lateinit var mockCoolShopDetailsUseCase: CoolShopDetailsUseCase

    @RelaxedMockK
    private lateinit var mockAddingToCartUseCase: AddingToCartUseCase

    @RelaxedMockK
    private lateinit var mockSavedStateHandle: SavedStateHandle

    private lateinit var viewModel: CoolShopDetailsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        // Настраиваем возвращаемое значение для ID
        every { mockSavedStateHandle.get<String>(CoolShopDetailsViewModel.ID) } returns "123"

        // Создаем экземпляр ViewModel
        viewModel = CoolShopDetailsViewModel(mockCoolShopDetailsUseCase, mockAddingToCartUseCase, mockSavedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Сбрасываем основной диспетчер
        clearAllMocks() // Очищаем моки
    }

    @Test
    fun `loadSelectedProduct() should emit Success state when useCase returns product`() = runTest {
        // Given: создаем мок CoolShopModel
        val product = mockk<CoolShopModel> {
            every { id } returns 123
            every { title } returns "Test Product"
            every { price } returns 99.99
            every { description } returns "This is a test product"
        }

        // Мокаем выполнение useCase для возврата потока с объектом CoolShopModel
        every { mockCoolShopDetailsUseCase.execute("123") } returns flowOf(product)

        // Когда: вызываем метод loadSelectedProduct
        viewModel.loadSelectedProduct("123")

        // Используем collect для асинхронного получения состояния
        var emittedState: ApiState<CoolShopModel>? = null
        val job = launch {
            viewModel.postStateFlow.collect { state ->
                emittedState = state // Сохраняем текущее состояние
            }
        }

        // Завершаем корутины
        advanceUntilIdle()
        job.cancel() // Завершаем коллекцию после завершения теста

        // Отладка: вывод текущего состояния
        println("Emitted state: $emittedState")

        // Then: проверяем, что состояние изменилось на Success с продуктом
        assertEquals(ApiState.Success(product), emittedState)

        verify { mockCoolShopDetailsUseCase.execute("123") } // Проверяем вызов useCase
    }

    @Test
    fun `loadSelectedProduct() should emit Failure state when useCase throws exception`() = runTest {
        // Given: создаем исключение
        val exception = Exception("Failed to load product")

        // Мокаем выполнение useCase для возврата потока, который выбрасывает исключение
        every { mockCoolShopDetailsUseCase.execute("123") } returns flow {
            throw exception
        }

        // Когда: вызываем метод loadSelectedProduct
        viewModel.loadSelectedProduct("123")

        // Используем collect для асинхронного получения состояния
        var emittedState: ApiState<CoolShopModel>? = null
        val job = launch {
            viewModel.postStateFlow.collect { state ->
                emittedState = state // Сохраняем текущее состояние
            }
        }

        // Завершаем корутины
        advanceUntilIdle()
        job.cancel() // Завершаем коллекцию после завершения теста

        // Отладка: вывод текущего состояния
        println("Emitted state: $emittedState")

        // Then: проверяем, что состояние изменилось на Failure с исключением
        assertEquals(ApiState.Failure(exception), emittedState)

        verify { mockCoolShopDetailsUseCase.execute("123") } // Проверяем вызов useCase
    }

    @Test
    fun `addToCardProduct() should call addingToCartUseCase`() = runTest {
        // Given: создаем мок CoolShopDBO
        val productDBO = mockk<CoolShopDBO>(relaxed = true)

        // When: вызываем метод addToCardProduct
        viewModel.addToCardProduct(productDBO)

        // Завершаем корутины
        advanceUntilIdle() // Это помогает дождаться завершения всех активных корутин

        // Then: проверяем, что добавление в корзину произошло
        coVerify { mockAddingToCartUseCase.execute(productDBO) }
    }
}
