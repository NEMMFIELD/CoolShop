package com.example.coolshop.details.data

import com.example.coolshop.api.models.ProductDTO
import com.example.database.models.CoolShopDBO

interface CoolShopDetailsRepository {
    suspend fun loadProductDetails(id:String): ProductDTO
    suspend fun addToCart(product:CoolShopDBO)
}
