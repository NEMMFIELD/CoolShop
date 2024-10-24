package com.example.coolshop.main.domain

import android.content.SharedPreferences
import com.example.data.CoolShopModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test


class CoolShopFavouritesProductsUseCaseTest {
    private val sharedPreferences: SharedPreferences = mockk(relaxed = true)
    private val repository: CoolShopRepository = mockk(relaxed = true) {
        every { setFavourites(any()) } answers {

        }
    }
    private val useCase = CoolShopFavouritesProductsUseCase(repository)

    @Test
    fun `should call setFavourites with correct CoolShopModel`() {
        // Given
        val coolShopModel  = CoolShopModel(
            id = 1,
            title = "Product 1",
            imgPath = "image1.jpg",
            price = 10.0,
            rate = 4.5,
            isLiked = true,
            description = "Sample description",
            category = "Sample category"
        )

        // When
        useCase.execute(coolShopModel)

        // Then
        verify { repository.setFavourites(coolShopModel) }
    }
}
