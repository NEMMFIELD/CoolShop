package com.example.coolshop.details.data

import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.ProductDTO
import com.example.coolshop.details.domain.CoolShopDetailsRepository
import com.example.database.dao.CoolShopDao
import com.example.database.models.CoolShopDBO
import javax.inject.Inject

internal class CoolShopDetailsRepositoryImpl @Inject constructor(
    private val api: CoolShopApi,
    private val dao: CoolShopDao
) : CoolShopDetailsRepository {

    override suspend fun loadProductDetails(id: String): ProductDTO {
        return api.getProduct(id)
    }

    override suspend fun addToCart(product: CoolShopDBO) {
        dao.insert(product)
    }
}
