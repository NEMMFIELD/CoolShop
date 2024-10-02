package com.example.coolshop.main.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.coolshop.main.domain.CoolShopCategoryUseCase
import com.example.coolshop.main.domain.CoolShopFavouritesProductsUseCase
import com.example.coolshop.main.domain.CoolShopProductsUseCase
import com.example.coolshop.main.domain.LoadFavouritesProductsUseCase
import com.example.data.CoolShopModel
import com.example.state.ApiState
import com.example.utils.Logger
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class CoolShopViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @MockK
    private lateinit var coolShopProductsUseCase: CoolShopProductsUseCase

    @MockK
    private lateinit var coolShopFavouritesProductsUseCase: CoolShopFavouritesProductsUseCase

    @MockK
    private lateinit var loadFavouritesProductsUseCase: LoadFavouritesProductsUseCase

    @MockK
    private lateinit var coolShopCategoryUseCase: CoolShopCategoryUseCase

    @MockK
    private lateinit var logger: Logger

    private lateinit var viewModel: CoolShopViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = CoolShopViewModel(
            coolShopProductsUseCase,
            coolShopFavouritesProductsUseCase,
            loadFavouritesProductsUseCase,
            coolShopCategoryUseCase,
            logger
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadProducts should emit success when products are loaded`() = runTest {
        // Given
        val products = listOf(
            CoolShopModel(
                id = 1,
                title = "Product 1",
                imgPath = "",
                price = 100.0,
                rate = 4.5,
                isLiked = false,
                description = "Test Product",
                category = "Category"
            )
        )
        coEvery { coolShopProductsUseCase.execute() } returns flowOf(products)

        // When
        viewModel.loadProducts()

        // Then
        coVerify { coolShopProductsUseCase.execute() }
        assertEquals(ApiState.Success(products), viewModel.postStateFlow.value)
    }

    @Test
    fun `loadProducts should emit failure when exception is thrown`() = runTest {
        // Given
        val exception = Exception("Error loading products")
        coEvery { coolShopProductsUseCase.execute() } throws exception

        // When
        viewModel.loadProducts()

        // Then
        coVerify { coolShopProductsUseCase.execute() }
        assertEquals(ApiState.Failure(exception), viewModel.postStateFlow.value)
    }

    @Test
    fun `loadCategoryProducts should emit success when category products are loaded`() = runTest {
        // Given
        val category = "electronics"
        val products = listOf(
            CoolShopModel(
                id = 1,
                title = "Product 1",
                imgPath = "",
                price = 100.0,
                rate = 4.5,
                isLiked = false,
                description = "Test Product",
                category = category
            )
        )
        coEvery { coolShopCategoryUseCase.execute(category) } returns flowOf(products)

        // When
        viewModel.loadCategoryProducts(category)

        // Then
        coVerify { coolShopCategoryUseCase.execute(category) }
        assertEquals(ApiState.Success(products), viewModel.postStateFlow.value)
    }

    @Test
    fun `setFavourites should call favourites use case`() = runTest {
        // Given
        val product = CoolShopModel(
            id = 1,
            title = "Product 1",
            imgPath = "",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Test Product",
            category = "Category"
        )
        coEvery { coolShopFavouritesProductsUseCase.execute(product) } just Runs

        // When
        viewModel.setFavourites(product)

        // Then
        verify { coolShopFavouritesProductsUseCase.execute(product) }
    }

    @Test
    fun `loadFavourites should call load favourites use case`() = runTest {
        // Given
        val product = CoolShopModel(
            id = 1,
            title = "Product 1",
            imgPath = "",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Test Product",
            category = "Category"
        )
        coEvery { loadFavouritesProductsUseCase.execute(product) } just Runs

        // When
        viewModel.loadFavourites(product)

        // Then
        verify { loadFavouritesProductsUseCase.execute(product) }
    }
}
