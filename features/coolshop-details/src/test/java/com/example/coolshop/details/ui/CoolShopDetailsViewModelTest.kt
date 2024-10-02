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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
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
import kotlin.test.assertTrue


class CoolShopDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var coolShopDetailsUseCase: CoolShopDetailsUseCase

    @MockK
    private lateinit var addingToCartUseCase: AddingToCartUseCase

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: CoolShopDetailsViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf(CoolShopDetailsViewModel.ID to "123"))
        viewModel = CoolShopDetailsViewModel(coolShopDetailsUseCase, addingToCartUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSelectedProduct should emit success when product is loaded`() = runTest {
        // Given
        val coolShopModel = CoolShopModel(
            id = 123, title = "Product", imgPath = "", price = 100.0, rate = 4.5,
            isLiked = false, description = "Test product", category = "Category"
        )
        coEvery { coolShopDetailsUseCase.execute("123") } returns flowOf(coolShopModel)

        // When
        viewModel.loadSelectedProduct("123")

        // Then
        coVerify { coolShopDetailsUseCase.execute("123") }
        assertTrue(viewModel.postStateFlow.value is ApiState.Success)
        assertEquals((viewModel.postStateFlow.value as ApiState.Success).data, coolShopModel)
    }

    @Test
    fun `loadSelectedProduct should emit failure when exception is thrown`() = runTest {
        // Given
        val exception = Exception("Error loading product")
        coEvery { coolShopDetailsUseCase.execute("123") } throws exception

        // When
        viewModel.loadSelectedProduct("123")

        // Then
        coVerify { coolShopDetailsUseCase.execute("123") }
        assertTrue(viewModel.postStateFlow.value is ApiState.Failure)
        assertEquals((viewModel.postStateFlow.value as ApiState.Failure).message, exception)
    }

    @Test
    fun `addToCardProduct should call addingToCartUseCase`() = runTest {
        // Given
        val coolShopDBO = CoolShopDBO(
            id = 1, title = "Product", imgPath = "", price = 100.0, rate = 4.5,
            isLiked = false, description = "Test product", category = "Category"
        )
        coEvery { addingToCartUseCase.execute(coolShopDBO) } just Runs

        // When
        viewModel.addToCardProduct(coolShopDBO)

        // Then
        coVerify { addingToCartUseCase.execute(coolShopDBO) }
    }
}
