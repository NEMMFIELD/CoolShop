package com.example.coolshop.main.domain

import com.example.coolshop.main.utils.Mapper
import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoolShopProductsUseCase @Inject internal constructor (private val repository: CoolShopRepository) {
   internal fun execute(): Flow<List<CoolShopModel>> = flow {
        val dto = repository.loadProducts()
        val data = dto.map { Mapper.mapDTOtoModel(it) }
        emit(data)
    }.flowOn(Dispatchers.IO)
}
