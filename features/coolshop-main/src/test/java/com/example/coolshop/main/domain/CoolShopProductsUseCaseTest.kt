package com.example.coolshop.main.domain


import com.example.coolshop.api.models.ProductDTO
import com.example.coolshop.main.data.CoolShopMapper
import com.example.coolshop.main.data.CoolShopRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class CoolShopProductsUseCaseTest {

    private val repository: CoolShopRepository = mockk()
    private val useCase = CoolShopProductsUseCase(repository)

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original
        testDispatcher.cancel()
    }

    @Test
    fun `should return mapped CoolShopModel list from repository products`() = runBlocking {
        // Given
        val responseItems = listOf(
            ProductDTO(id = 1, title = "Product 1", price = 10.0, image = "image1.jpg"),
            ProductDTO(id = 2, title = "Product 2", price = 15.0, image = "image2.jpg")
        )

       // val myList = listOf<ResponseItem>()

        // Mocking the repository response
        coEvery { repository.loadProducts() } returns responseItems

        // Mocking the Mapper functionality
        val expectedModels = responseItems.map { CoolShopMapper.mapDTOToModel(it) }

        // When
        val result = useCase.execute()

        // Then
        result.collect { actualModels ->
            assertEquals(expectedModels, actualModels)
        }

        // Verify that the repository's loadProducts was called
        coVerify { repository.loadProducts() }
    }
}
