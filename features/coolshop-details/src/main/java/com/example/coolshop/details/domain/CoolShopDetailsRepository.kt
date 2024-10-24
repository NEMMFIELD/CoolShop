package com.example.coolshop.details.domain

import com.example.coolshop.api.models.ProductDTO
import com.example.database.models.CoolShopDBO

internal interface CoolShopDetailsRepository {
    suspend fun loadProductDetails(id:String): ProductDTO
    suspend fun addToCart(product:CoolShopDBO)
}
