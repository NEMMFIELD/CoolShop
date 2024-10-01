package com.example.coolshop.details.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.coolshop.details.domain.AddingToCartUseCase
import com.example.coolshop.details.domain.CoolShopDetailsUseCase
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals


class CoolShopDetailsViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CoolShopDetailsViewModel
    private val useCase: CoolShopDetailsUseCase = mockk()
    private val addingToCartUseCase: AddingToCartUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    @Before
    fun setUp() {
        // Инициализация ViewModel с замокированными зависимостями
        viewModel = CoolShopDetailsViewModel(useCase, addingToCartUseCase, savedStateHandle)

        // Настройка savedStateHandle для возврата ID
        every { savedStateHandle.get<String>(CoolShopDetailsViewModel.ID) } returns "1"
    }

    @Test
    fun `loadSelectedProduct should emit ApiState Success when product is loaded successfully`() =
        runTest {
            // Arrange
            val expectedProduct = CoolShopModel(
                id = 1,
                title = "Product 1",
                imgPath = "/img1.jpg",
                price = 100.0,
                rate = 4.5,
                isLiked = false,
                description = "Product 1 description",
                category = "Category 1"
            )
            every { useCase.execute("1") } returns flowOf(expectedProduct)

            // Act
            viewModel.postStateFlow.collect { state ->
                // Assert
                if (state is ApiState.Success) {
                    assertEquals(expectedProduct, state.data)
                }
            }
        }

    @Test
    fun `loadSelectedProduct should emit ApiState Failure when an error occurs`() = runTest {
        // Arrange
        val exception = Exception("Error loading product")
        every { useCase.execute("111") } throws exception

        // Act
        viewModel.postStateFlow.collect { state ->
            // Assert
            if (state is ApiState.Failure) {
                assertEquals(exception, state.message)
            }
        }
    }

    @Test
    fun `addToCartProduct should call addingToCartUseCase execute`() = runTest {
        // Arrange
        val productToAdd = CoolShopDBO(
            id = 1,
            title = "Product 1",
            imgPath = "/img1.jpg",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Product 1 description",
            category = "Category 1"
        )

        // Act
        viewModel.addToCardProduct(productToAdd)

        // Assert
        coVerify { addingToCartUseCase.execute(productToAdd) }
    }
}
