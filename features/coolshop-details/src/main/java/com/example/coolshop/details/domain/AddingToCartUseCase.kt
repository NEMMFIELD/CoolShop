package com.example.coolshop.details.domain

import com.example.database.models.CoolShopDBO
import javax.inject.Inject

internal class AddingToCartUseCase @Inject constructor(private val repository: CoolShopDetailsRepository) {

    suspend fun execute(product: CoolShopDBO) {
        repository.addToCart(product)
    }
}
