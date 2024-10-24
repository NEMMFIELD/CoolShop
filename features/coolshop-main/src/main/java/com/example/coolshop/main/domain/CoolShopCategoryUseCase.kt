package com.example.coolshop.main.domain


import com.example.coolshop.main.data.CoolShopMapper
import com.example.data.CoolShopModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class CoolShopCategoryUseCase @Inject constructor(private val repository: CoolShopRepository) {

    internal fun execute(category: String): Flow<List<CoolShopModel>> = flow {
        val categoriesListProducts = repository.loadCategory(category)
        val categoryProducts = categoriesListProducts.map { categoryProduct ->
            CoolShopMapper.mapDTOToModel(categoryProduct)
        }
        emit(categoryProducts)
    }.flowOn(Dispatchers.IO)
}
