package com.example.coolshop.main.data

import com.example.coolshop.api.models.ProductDTO
import com.example.data.CoolShopModel

interface CoolShopRepository {
   suspend fun loadProducts():List<ProductDTO>
    fun setFavourites(coolShopModel:CoolShopModel)
    fun getFavourites(id:String): Boolean
    fun loadFavourites(coolShopModel: CoolShopModel)
    suspend fun loadCategory(category:String):List<ProductDTO>
}
