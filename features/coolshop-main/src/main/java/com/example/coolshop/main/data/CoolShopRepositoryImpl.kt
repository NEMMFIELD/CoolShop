package com.example.coolshop.main.data

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.ProductDTO
import com.example.coolshop.main.domain.CoolShopRepository
import com.example.data.CoolShopModel
import javax.inject.Inject


internal class CoolShopRepositoryImpl @Inject constructor(
    private val api: CoolShopApi,
    private val sharedPreferences: SharedPreferences,
) : CoolShopRepository {

    override suspend fun loadProducts(): List<ProductDTO> {
        return api.getAllProducts()
    }

    override fun setFavourites(coolShopModel: CoolShopModel) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(coolShopModel.id.toString(), coolShopModel.isLiked).apply()
    }

    override fun getFavourites(id: String): Boolean {
        return sharedPreferences.getBoolean(id, false)
    }

    override fun loadFavourites(coolShopModel: CoolShopModel) {
        coolShopModel.isLiked = getFavourites(id = coolShopModel.id.toString())
    }

    override suspend fun loadCategory(category: String): List<ProductDTO> {
        return api.getCategory(category)
    }
}

