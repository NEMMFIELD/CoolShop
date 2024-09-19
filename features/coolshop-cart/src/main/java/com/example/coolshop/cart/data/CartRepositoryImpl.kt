package com.example.coolshop.cart.data

import com.example.coolshop.cart.domain.CartRepository
import com.example.data.CoolShopModel
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val dao: CoolShopDao) : CartRepository {
    override var flowCart: Flow<List<CoolShopDBO>> = dao.observeAll()
    override var flowSum: Flow<Double?> = flowCart.map { list ->
        list.map { listByPrice ->
            listByPrice.price
        }.sumOf { result ->
            result ?: 0.0
        }
    }

    override suspend fun loadProductsFromDatabase(): List<CoolShopModel> {
        val dbList = dao.getAll()
        var models: List<CoolShopModel> = ArrayList()
        dbList.forEach {
            models = listOf<CoolShopModel>(com.example.utils.Mapper.mapModelDBOToModel(it))
        }
        return models
    }

    override suspend fun removeProductFromCart(product: CoolShopDBO) {
        dao.remove(product)
    }
}
