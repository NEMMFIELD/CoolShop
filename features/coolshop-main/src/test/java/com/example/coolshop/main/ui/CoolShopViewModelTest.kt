package com.example.coolshop.main.ui

import com.example.coolshop.main.domain.CoolShopCategoryUseCase
import com.example.coolshop.main.domain.CoolShopFavouritesProductsUseCase
import com.example.coolshop.main.domain.CoolShopProductsUseCase
import com.example.coolshop.main.domain.LoadFavouritesProductsUseCase
import com.example.data.CoolShopModel
import com.example.state.State
import com.example.utils.Logger
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class CoolShopViewModelTest {
    private lateinit var viewModel: CoolShopViewModel

    // Mocked dependencies
    private val mockUseCase: CoolShopProductsUseCase = mockk()
    private val mockUseCaseFavourites: CoolShopFavouritesProductsUseCase = mockk()
    private val mockLoadFavouritesProductsUseCase: LoadFavouritesProductsUseCase = mockk()
    private val mockCategoryUseCase: CoolShopCategoryUseCase = mockk()
    private val mockLogger: Logger = mockk(relaxed = true) // Relaxed to allow logging without explicit stubbing

    // Test dispatcher for coroutines
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel with mocked dependencies
        viewModel = CoolShopViewModel(
            useCase = mockUseCase,
            useCaseFavourites = mockUseCaseFavourites,
            loadFavouritesProductsUseCase = mockLoadFavouritesProductsUseCase,
            categoryUseCase = mockCategoryUseCase,
            logger = mockLogger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts() should emit Success state when useCase returns products`() = runTest {
        // Given
        val products = listOf(mockk<CoolShopModel>())
        every { mockUseCase.execute() } returns flowOf(products)

        // Когда
        val states = mutableListOf<State<List<CoolShopModel>>>()

        // Собираем данные из поста
        val job = launch {
            viewModel.postProducts.collect {
                states.add(it) // Добавляем каждое изменение состояния
            }
        }

        // Явно вызываем загрузку продуктов
        viewModel.loadProducts()

        // Ожидаем завершения корутин
        advanceUntilIdle()

        // Проверяем, что состояние изменилось с Empty на Success
        assertEquals(listOf(State.Empty, State.Success(products)), states)

        job.cancel() // Останавливаем сбор данных

        // Проверяем, что useCase был вызван
        verify { mockUseCase.execute() }
        confirmVerified(mockUseCase)
    }

    @Test
    fun `loadProducts() should emit Failure state when useCase throws exception`() = runTest {
        // Given
        val exception = Exception("Failed to load products")

        // Устанавливаем, что useCase выбрасывает исключение при вызове execute()
        every { mockUseCase.execute() } throws exception

        // Когда
        val states = mutableListOf<State<List<CoolShopModel>>>()

        // Собираем изменения в postStateFlow
        val job = launch {
            viewModel.postProducts.collect { state ->
                states.add(state) // Добавляем каждое изменение состояния в список
            }
        }

        // Явно вызываем метод loadProducts(), который должен привести к ошибке
        viewModel.loadProducts()

        // Ожидаем завершения всех корутин
        advanceUntilIdle()

        // Проверяем, что состояний два: Empty и Failure
        assertEquals(2, states.size)
        assertTrue(states[1] is State.Failure) // Проверяем, что второе состояние — это Failure

        // Сравниваем сообщение исключения
        val actualException = (states[1] as State.Failure).message
        assertEquals("Failed to load products", actualException.message) // Проверяем сообщение

        // Останавливаем сбор данных
        job.cancel()

        // Проверяем, что logger был вызван для обработки ошибки с использованием кастомного матчера
        verify {
            mockLogger.e(
                eq("CoolShopViewModel"),
                eq("Error loading products"),
                match { it is Exception && it.message == "Failed to load products" }
            )
        }
    }

    @Test
    fun `loadCategoryProducts() should emit Success state when categoryUseCase returns products`() = runTest {
        // Given
        val category = "Electronics"
        val products = listOf(mockk<CoolShopModel>())
        every { mockCategoryUseCase.execute(category) } returns flowOf(products)
        val states = mutableListOf<State<List<CoolShopModel>>>()
        val job = launch {
            viewModel.postProducts.collect { state ->
                states.add(state)
            }
        }
        // When
        viewModel.loadCategoryProducts(category)

        // Advance time to allow coroutine to complete
        advanceUntilIdle()

        // Then
        assertEquals(State.Success(products), viewModel.postProducts.value)
        job.cancel()
        verify { mockCategoryUseCase.execute(category) }
        confirmVerified(mockCategoryUseCase)
    }

    @Test
    fun `setFavourites() should execute useCaseFavourites`() {
        // Given
        val product = mockk<CoolShopModel>()
        every { mockUseCaseFavourites.execute(product) } just Runs

        // When
        viewModel.setFavourites(product)

        // Then
        verify { mockUseCaseFavourites.execute(product) }
        confirmVerified(mockUseCaseFavourites)
    }

    @Test
    fun `loadFavourites() should execute loadFavouritesProductsUseCase`() {
        // Given
        val product = mockk<CoolShopModel>()
        every { mockLoadFavouritesProductsUseCase.execute(product) } just Runs

        // When
        viewModel.loadFavourites(product)

        // Then
        verify { mockLoadFavouritesProductsUseCase.execute(product) }
        confirmVerified(mockLoadFavouritesProductsUseCase)
    }
}
