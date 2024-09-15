package com.example.coolshop.main.domain


import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CoolShopCategoryUseCase @Inject constructor(private val repository: CoolShopRepository) {
    internal fun execute(category:String): Flow<List<CoolShopModel>> = flow {
        val data = repository.loadCategory(category)
        val categoryLoad = data.map { com.example.utils.Mapper.mapDTOtoModel(it) }
        emit(categoryLoad)
    }.flowOn(Dispatchers.IO)
}
