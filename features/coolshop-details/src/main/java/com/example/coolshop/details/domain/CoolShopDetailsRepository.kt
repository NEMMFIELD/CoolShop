package com.example.coolshop.details.domain

import com.example.coolshop.api.models.ResponseItem
import com.example.database.models.CoolShopDBO

interface CoolShopDetailsRepository {
    suspend fun loadProductDetails(id:String): ResponseItem
    suspend fun addToCart(product:CoolShopDBO)
}
