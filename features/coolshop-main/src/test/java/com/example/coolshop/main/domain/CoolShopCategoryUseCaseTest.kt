package com.example.coolshop.main.domain

import com.example.coolshop.api.models.Rating
import com.example.coolshop.api.models.ProductDTO
import com.example.coolshop.main.data.CoolShopMapper
import com.example.coolshop.main.data.CoolShopRepository
import com.example.data.CoolShopModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class CoolShopCategoryUseCaseTest{
    private lateinit var useCase: CoolShopCategoryUseCase
    private val repository: CoolShopRepository = mockk()

    // Тестовые данные
    private val testCategory = "electronics"
    private val responseItemList = listOf(
        ProductDTO(
            id = 1,
            title = "Phone",
            image = "img/path",
            price = 699.99,
            description = "Smartphone",
            category = "electronics",
            rating = Rating(rate = 4.5, count = 100)
        ),
        ProductDTO(
            id = 2,
            title = "Laptop",
            image = "img/path",
            price = 1299.99,
            description = "Laptop",
            category = "electronics",
            rating = Rating(rate = 4.8, count = 50)
        )
    )
    private val mappedModels = responseItemList.map {
        CoolShopModel(
            id = it.id,
            title = it.title,
            imgPath = it.image,
            price = it.price,
            rate = it.rating?.rate,
            isLiked = false, // Предположим, что это не избранное по умолчанию
            description = it.description,
            category = it.category
        )
    }

    @Before
    fun setUp() {
        useCase = CoolShopCategoryUseCase(repository)
        Dispatchers.setMain(StandardTestDispatcher()) // Используем TestCoroutineDispatcher для тестов корутин
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Сбрасываем диспетчер после тестов
    }

    @Test
    fun `execute should return mapped category products`() = runBlocking {
        // Мокаем метод loadCategory() репозитория
        coEvery { repository.loadCategory(testCategory) } returns responseItemList

        // Мокаем маппер
        mockkObject(CoolShopMapper)
        responseItemList.forEachIndexed { index, item ->
            coEvery { CoolShopMapper.mapDTOToModel(item) } returns mappedModels[index]
        }

        // Выполняем use case
        val result = useCase.execute(testCategory).first()

        // Проверяем результат
        assertEquals(mappedModels, result)
    }
}
