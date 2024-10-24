package com.example.coolshop.details.data

import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.Rating
import com.example.coolshop.api.models.ProductDTO
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals


class CoolShopDetailsRepositoryImplTest {

    private lateinit var api: CoolShopApi
    private lateinit var dao: CoolShopDao
    private lateinit var repository: CoolShopDetailsRepositoryImpl

    @Before
    fun setUp() {
        // Создаем моки для api и dao
        api = mockk()
        dao = mockk()
        // Инициализируем репозиторий с моками
        repository = CoolShopDetailsRepositoryImpl(api, dao)
    }

    @Test
    fun `loadProductDetails should return ResponseItem when API call is successful`() = runTest {
        // Given
        val id = "1"
        val expectedResponse = ProductDTO(
            id = 1,
            title = "Product 1",
            image = "/img1.jpg",
            price = 100.0,
            rating = Rating(rate = 4.5, count = 10),
            description = "Product 1 description",
            category = "Category 1"
        )

        // Настройка мока для API
        coEvery { api.getProduct(id) } returns expectedResponse

        // When
        val actualResponse = repository.loadProductDetails(id)

        // Then
        assertEquals(expectedResponse, actualResponse)
        coVerify(exactly = 1) { api.getProduct(id) } // Проверяем, что API был вызван один раз
    }

    @Test
    fun `addToCart should call dao insert method`() = runTest {
        // Given
        val product = CoolShopDBO(
            id = 1,
            title = "Product 1",
            imgPath = "/img1.jpg",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Product 1 description",
            category = "Category 1"
        )


        // Настройка мока для dao (хотя мы не ожидаем возвращаемого значения)
        coEvery { dao.insert(product) } returns Unit

        // When
        repository.addToCart(product)

        // Then
        coVerify(exactly = 1) { dao.insert(product) } // Проверяем, что метод insert был вызван один раз
    }
}
