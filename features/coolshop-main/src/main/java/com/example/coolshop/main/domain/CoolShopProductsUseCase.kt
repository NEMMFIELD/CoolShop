package com.example.coolshop.main.domain


import com.example.coolshop.main.data.CoolShopMapper
import com.example.coolshop.main.data.CoolShopRepository
import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class CoolShopProductsUseCase @Inject internal constructor(private val repository: CoolShopRepository) {
    internal fun execute(): Flow<List<CoolShopModel>> = flow {
        val modelFromServer = repository.loadProducts()
        val products = modelFromServer.map { productsFromServer ->
            CoolShopMapper.mapDTOToModel(productsFromServer) }
        emit(products)
    }.flowOn(Dispatchers.IO)
}
