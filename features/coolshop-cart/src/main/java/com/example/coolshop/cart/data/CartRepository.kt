package com.example.coolshop.cart.data

import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    var flowCart: Flow<List<CoolShopDBO>>
    var flowSum: Flow<Double?>
    suspend fun loadProductsFromDatabase(): List<CoolShopModel>
    suspend fun removeProductFromCart(product: CoolShopDBO)
}
