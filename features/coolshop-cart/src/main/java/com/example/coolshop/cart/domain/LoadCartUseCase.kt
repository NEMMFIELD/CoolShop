package com.example.coolshop.cart.domain

import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoadCartUseCase @Inject constructor(private val repository: CartRepository){
    fun execute(): Flow<List<CoolShopModel>> = flow {
        val data = repository.loadProductsFromDatabase()
        emit(data)
    }.flowOn(Dispatchers.IO)
}
