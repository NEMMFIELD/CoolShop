package com.example.coolshop.main.domain

import com.example.data.CoolShopModel
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class LoadFavouritesProductsUseCaseTest {
    private lateinit var useCase: LoadFavouritesProductsUseCase
    private val repository: CoolShopRepository = mockk(relaxed = true) // relaxed позволяет избежать необходимости явно мокать все вызовы

    // Тестовые данные
    private val testModel = CoolShopModel(
        id = 1,
        title = "Phone",
        imgPath = "img/path",
        price = 699.99,
        rate = 4.5,
        isLiked = false,
        description = "Smartphone",
        category = "electronics"
    )

    @Before
    fun setUp() {
        useCase = LoadFavouritesProductsUseCase(repository)
    }

    @Test
    fun `execute should call repository loadFavourites`() {
        // Выполняем use case
        useCase.execute(testModel)

        // Проверяем, что метод loadFavourites был вызван с нужной моделью
        verify { repository.loadFavourites(testModel) }
    }
}
