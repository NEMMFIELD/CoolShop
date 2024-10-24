package com.example.coolshop.main.domain

import com.example.coolshop.main.data.CoolShopRepository
import com.example.data.CoolShopModel
import javax.inject.Inject

internal class CoolShopFavouritesProductsUseCase @Inject internal constructor(private val repository: CoolShopRepository) {
     fun execute(coolShopModel: CoolShopModel) {
        repository.setFavourites(coolShopModel)
    }
}
