package com.example.coolshop.cart.data

import android.util.Log
import com.example.coolshop.cart.domain.CartRepository
import com.example.data.CoolShopModel
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CartRepositoryImpl @Inject constructor(private val dao: CoolShopDao?) :
    CartRepository {
    override var flowCart: Flow<List<CoolShopDBO>>? = dao?.observeAll()

    override var flowSum: Flow<Double?>? = flowCart?.map { listProducts ->
        listProducts.map { listPrices ->
            listPrices.price
        }.sumOf { totalSum ->
            totalSum ?: 0.0
        }
    }

    override suspend fun loadProductsFromDatabase(): List<CoolShopModel> {
        val productsFromDatabase = dao?.getAll()
        var productsList: List<CoolShopModel> = ArrayList()

        productsFromDatabase?.forEach {
            productsList = listOf(CartMapper.mapModelDBOToModel(it))
        }
        return productsList
    }

    override suspend fun removeProductFromCart(product: CoolShopDBO) {
        dao?.remove(product) ?: Log.d("CoolShopCart", "CoolShopDao reference is null")
    }
}
