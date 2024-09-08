package com.example.coolshop.main.domain

import com.example.data.CoolShopModel
import javax.inject.Inject

class LoadFavouritesProductsUseCase @Inject internal constructor(private val repository: CoolShopRepository){
    fun execute(coolShopModel: CoolShopModel) {
        repository.loadFavourites(coolShopModel)
    }
}
