package com.example.coolshop.details.domain

import com.example.coolshop.main.utils.Mapper
import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoolShopDetailsUseCase @Inject constructor (private val repositoryImpl: CoolShopDetailsRepository) {
    fun execute(id:String): Flow<CoolShopModel> = flow {
        val model = repositoryImpl.loadProductDetails(id)
        val details = Mapper.mapDTOtoModel(model)
        emit(details)
    }.flowOn(Dispatchers.IO)
}
