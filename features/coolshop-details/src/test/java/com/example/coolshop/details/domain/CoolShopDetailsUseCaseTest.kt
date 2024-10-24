package com.example.coolshop.details.domain

import com.example.coolshop.api.models.Rating
import com.example.coolshop.api.models.ProductDTO
import com.example.coolshop.details.data.CoolShopDetailsMapper
import com.example.coolshop.details.data.CoolShopDetailsRepository
import com.example.data.CoolShopModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith


class CoolShopDetailsUseCaseTest {
    // Мокируем репозиторий
    private lateinit var repository: CoolShopDetailsRepository
    private lateinit var useCase: CoolShopDetailsUseCase

    @Before
    fun setUp() {
        // Создаем моки для репозитория и маппера
        repository = mockk()
        mockkObject(CoolShopDetailsMapper)

        // Инициализируем useCase с мокнутым репозиторием
        useCase = CoolShopDetailsUseCase(repository)

        // Переключаем диспетчеры для тестов
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `execute should return CoolShopModel for valid id`() = runTest {
        // Given
        val id = "1"

        // Создаем реальный объект ResponseItem
        val dto = ProductDTO(
            id = 1,
            title = "Product 1",
            image = "/img1.jpg",
            price = 100.0,
            rating = Rating(rate = 4.5, count = 10),
            description = "Product 1 description",
            category = "Category 1"
        )

        // Создаем реальный объект CoolShopModel
        val model = CoolShopModel(
            id = 1,
            title = "Product 1",
            imgPath = "/img1.jpg",
            price = 100.0,
            rate = 4.5,
            isLiked = false,
            description = "Product 1 description",
            category = "Category 1"
        )

        // Мокируем вызов репозитория, чтобы он возвращал реальный объект DTO
        coEvery { repository.loadProductDetails(id) } returns dto

        // Мокируем вызов маппера, чтобы он преобразовывал DTO в модель
        coEvery { CoolShopDetailsMapper.mapDTOToModel(dto) } returns model

        // When
        val result = useCase.execute(id).first()

        // Then
        assertEquals(model, result) // Проверяем, что результат соответствует ожидаемой модели

        // Проверяем, что метод репозитория был вызван один раз с правильным id
        coVerify(exactly = 1) { repository.loadProductDetails(id) }
    }

    @Test
    fun `execute should handle repository exception`() = runTest {
        val id = "2"
        val exception = RuntimeException("Error loading details")

        // Мокируем ошибку при выполнении метода репозитория
        coEvery { repository.loadProductDetails(id) } throws exception

        // Используем assertFailsWith для проверки выброса исключения
        val actualException = assertFailsWith<RuntimeException> {
            useCase.execute(id).first()
        }

        assertEquals("Error loading details", actualException.message)

        // Проверяем, что метод репозитория был вызван один раз
        coVerify(exactly = 1) { repository.loadProductDetails(id) }
    }
}
