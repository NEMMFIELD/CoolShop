package com.example.coolshop.main.domain

import com.example.coolshop.api.models.ResponseItem
import com.example.data.CoolShopModel

interface CoolShopRepository {
   suspend fun loadProducts():List<ResponseItem>
    fun setFavourites(coolShopModel:CoolShopModel)
    fun getFavourites(id:String): Boolean
    fun loadFavourites(coolShopModel: CoolShopModel)
    suspend fun loadCategory(category:String):List<ResponseItem>
}
