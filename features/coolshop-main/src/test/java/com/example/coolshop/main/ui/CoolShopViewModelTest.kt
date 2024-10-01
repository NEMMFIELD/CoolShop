package com.example.coolshop.main.ui

import com.example.coolshop.main.domain.CoolShopCategoryUseCase
import com.example.coolshop.main.domain.CoolShopFavouritesProductsUseCase
import com.example.coolshop.main.domain.CoolShopProductsUseCase
import com.example.coolshop.main.domain.LoadFavouritesProductsUseCase
import com.example.data.CoolShopModel
import com.example.state.ApiState
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue


@Suppress("UNREACHABLE_CODE")
@ExperimentalCoroutinesApi
class CoolShopViewModelTest  {
    private lateinit var viewModel: CoolShopViewModel
    private val useCase: CoolShopProductsUseCase = mockk()
    private val useCaseFavourites: CoolShopFavouritesProductsUseCase = mockk()
    private val loadFavouritesProductsUseCase: LoadFavouritesProductsUseCase = mockk()
    private val categoryUseCase: CoolShopCategoryUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CoolShopViewModel(
            useCase,
            useCaseFavourites,
            loadFavouritesProductsUseCase,
            categoryUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `loadProducts should emit success when useCase returns products`() = testScope.runTest {
        val products = listOf(
            CoolShopModel(1, "Product 1", "/img1.jpg", 100.0, 4.5, false, "Description 1", "Category 1"),
            CoolShopModel(2, "Product 2", "/img2.jpg", 150.0, 4.0, true, "Description 2", "Category 2")
        )

        // Мокируем выполнение useCase, чтобы вернуть поток с продуктами
        coEvery { useCase.execute() } returns flow { emit(products) }

        // When
        viewModel.loadProducts()

        // Запускаем корутины и ждем их завершения
        advanceUntilIdle()

        // Then
        val result = viewModel.postStateFlow.value

        // Проверка что результат - это Success с правильными данными
        assertTrue(result is ApiState.Success, "Expected Success state but got: $result")
        assertEquals(products, (result as ApiState.Success).data)

        // Проверяем, что useCase.execute() был вызван один раз
        coVerify(exactly = 1) { useCase.execute() }
    }

    @Test
    fun `loadProducts should emit failure when useCase throws an exception`() = testScope.runTest {
        // Given
        val exception = Exception("Something went wrong")
        coEvery { useCase.execute() } throws exception

        // When
        viewModel.loadProducts()

        // Advance time to simulate the delay in coroutine
        advanceUntilIdle()

        // Then
        val result = viewModel.postStateFlow.value
        assertTrue(result is ApiState.Failure)
        assertEquals(exception, (result as ApiState.Failure).message)

        coVerify(exactly = 1) { useCase.execute() }
    }

    @Test
    fun `loadCategoryProducts should emit success when categoryUseCase returns products`() = testScope.runTest {
        // Given
        val category = "Category 1"
        val products = listOf(
            CoolShopModel(1, "Product A", "/imgA.jpg", 120.0, 4.8, false, "Description A", "Category 1"),
            CoolShopModel(2, "Product B", "/imgB.jpg", 130.0, 4.2, true, "Description B", "Category 1")
        )
        coEvery { categoryUseCase.execute(category) } returns flow { emit(products) }

        // When
        viewModel.loadCategoryProducts(category)

        // Advance time to simulate the delay in coroutine
        advanceUntilIdle()

        // Then
        val result = viewModel.postStateFlow.value
        assertTrue(result is ApiState.Success)
        assertEquals(products, (result as ApiState.Success).data)

        coVerify(exactly = 1) { categoryUseCase.execute(category) }
    }

    @Test
    fun `setFavourites should call useCaseFavourites execute`() = testScope.runTest {
        // Given
        val coolShopModel = CoolShopModel(1, "Favourite Product", "/img.jpg", 200.0, 4.9, true, "Favourite Description", "Favourite Category")
        every { useCaseFavourites.execute(coolShopModel) } just Runs

        // When
        viewModel.setFavourites(coolShopModel)

        // Then
        verify(exactly = 1) { useCaseFavourites.execute(coolShopModel) }
    }

    @Test
    fun `loadFavourites should call loadFavouritesProductsUseCase execute`() = testScope.runTest {
        // Given
        val coolShopModel = CoolShopModel(1, "Favourite Product", "/img.jpg", 200.0, 4.9, true, "Favourite Description", "Favourite Category")
        every { loadFavouritesProductsUseCase.execute(coolShopModel) } just Runs

        // When
        viewModel.loadFavourites(coolShopModel)

        // Then
        verify(exactly = 1) { loadFavouritesProductsUseCase.execute(coolShopModel) }
    }
}
