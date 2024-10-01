package com.example.coolshop.main.data.models

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.ResponseItem
import com.example.data.CoolShopModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class CoolShopRepositoryImplTest {

    private lateinit var repository: CoolShopRepositoryImpl
    private val api: CoolShopApi = mockk()
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = CoolShopRepositoryImpl(api, sharedPreferences)
    }

    @After
    fun tearDown() {
        unmockkAll() // Сбросить все мок-объекты после каждого теста
    }

    @Test
    fun `loadProducts should return list of ResponseItem from api`() = runBlocking {
        // Устанавливаем мокаемое значение для api.getAllProducts()
        val mockResponse = listOf(
            ResponseItem(id = 1, title = "Product 1", price = 100.0),
            ResponseItem(id = 2, title = "Product 2", price = 200.0)
        )
        coEvery { api.getAllProducts() } returns mockResponse

        // Вызываем метод и проверяем результат
        val result = repository.loadProducts()
        assertEquals(mockResponse, result)

        // Проверяем, что api.getAllProducts() был вызван
        coVerify { api.getAllProducts() }
    }

    @Test
    fun `setFavourites should save isLiked status in sharedPreferences`() {
        // Создаем тестовую модель
        val coolShopModel = CoolShopModel(id = 1, title = "Product 1", imgPath = "img/path", price = 100.0, rate = 4.5, isLiked = true)

        // Вызываем метод
        repository.setFavourites(coolShopModel)

        // Проверяем, что putBoolean был вызван с правильными параметрами
        verify { sharedPreferences.edit().putBoolean("1", true) }
    }

    @Test
    fun `getFavourites should return correct boolean value from sharedPreferences`() {
        // Устанавливаем мокаемое значение
        every { sharedPreferences.getBoolean("1", false) } returns true

        // Проверяем результат
        val result = repository.getFavourites("1")
        assertEquals(true, result)

        // Проверяем, что getBoolean был вызван
        verify { sharedPreferences.getBoolean("1", false) }
    }

    @Test
    fun `loadFavourites should set isLiked property based on sharedPreferences`() {
        // Создаем тестовую модель
        val coolShopModel = CoolShopModel(id = 1, title = "Product 1", imgPath = "img/path", price = 100.0, rate = 4.5, isLiked = false)

        // Устанавливаем мокаемое значение
        every { sharedPreferences.getBoolean("1", false) } returns true

        // Вызываем метод
        repository.loadFavourites(coolShopModel)

        // Проверяем, что isLiked обновился
        assertEquals(true, coolShopModel.isLiked)
    }

    @Test
    fun `loadCategory should return list of ResponseItem from api`() = runBlocking {
        // Устанавливаем мокаемое значение для api.getCategory()
        val mockCategoryResponse = listOf(
            ResponseItem(id = 1, title = "Product 1", price = 100.0),
            ResponseItem(id = 2, title = "Product 2", price = 200.0)
        )
        coEvery { api.getCategory("electronics") } returns mockCategoryResponse

        // Вызываем метод и проверяем результат
        val result = repository.loadCategory("electronics")
        assertEquals(mockCategoryResponse, result)

        // Проверяем, что api.getCategory() был вызван с правильным параметром
        coVerify { api.getCategory("electronics") }
    }
}
