package com.example.coolshop.cart.data

import com.example.data.CoolShopModel
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import kotlin.test.Test


class CartRepositoryImplTest{
    private lateinit var dao: CoolShopDao
    private lateinit var repository: CartRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = CartRepositoryImpl(dao)
    }

    @Test
    fun `flowCart should return correct list of products`() = runTest {
        // Given: мок данных, возвращаемых DAO
        val expectedProducts = listOf(
            CoolShopDBO(id = 1, title = "Product 1", price = 10.0, category = "electronics", description = "Description", isLiked = false, rate = 4.5, imgPath = "vkontakte/img"),
            CoolShopDBO(id = 2, title = "Product 2", price = 20.0,category = "electronics",description = "Description", isLiked = false, rate = 5.5, imgPath = "facebook/img"  )
        )
        every { dao.observeAll() } returns flowOf(expectedProducts)

        // When: получаем данные из flowCart
        val resultFlow = repository.flowCart

        // Then: collect и проверка результата
        resultFlow.collect { productList ->
            assertEquals(expectedProducts, productList)
        }

        // Verify: проверяем, что вызван метод DAO
        verify { dao.observeAll() }
    }

    @Test
    fun `flowSum should return correct sum of product prices`() = runTest {
        // Given: продукты с разными ценами
        val products = listOf(
            CoolShopDBO(id = 1, title = "Product 1", price = 10.0, category = "electronics", description = "Description", isLiked = false, rate = 4.5, imgPath = "vkontakte/img"),
            CoolShopDBO(id = 2, title = "Product 2", price = 20.0,category = "electronics",description = "Description", isLiked = false, rate = 5.5, imgPath = "facebook/img"  )
        )
        every { dao.observeAll() } returns flowOf(products)

        // When: получаем поток с суммой цен
        val resultFlow = repository.flowSum

        // Then: collect и проверка результата
        resultFlow.collect { totalSum ->
            assertEquals(30.0, totalSum!!, 0.0)
        }
    }

    @Test
    fun `loadProductsFromDatabase should return mapped products`() = runBlocking {
        // Given: мок списка продуктов из базы данных
        val dbProducts = listOf(
            CoolShopDBO(id = 1, title = "Product 1", price = 10.0, category = "electronics", description = "Description", isLiked = false, rate = 4.5, imgPath = "vkontakte/img"),
        )
        val expectedModel = listOf(
            CoolShopModel(id = 1, title = "Product 1", imgPath = "", price = 10.0, rate = 0.0, isLiked = false, description = "", category = "")
        )

        coEvery { dao.getAll() } returns dbProducts
        mockkObject(CartMapper)
        every { CartMapper.mapModelDBOToModel(any()) } returns expectedModel.first()

        // When: загружаем продукты из базы данных
        val result = repository.loadProductsFromDatabase()

        // Then: проверяем, что продукты были корректно преобразованы
        assertEquals(expectedModel, result)
    }

    @Test
    fun `removeProductFromCart should call dao remove`() = runBlocking {
        // Given: продукт, который нужно удалить
        val productToRemove = CoolShopDBO(id = 1, title = "Product 1", price = 10.0, category = "electronics", description = "Description", isLiked = false, rate = 4.5, imgPath = "vkontakte/img")

        // When: вызываем метод удаления продукта
        repository.removeProductFromCart(productToRemove)

        // Then: проверяем, что метод dao.remove() был вызван
        coVerify { dao.remove(productToRemove) }
    }
}
