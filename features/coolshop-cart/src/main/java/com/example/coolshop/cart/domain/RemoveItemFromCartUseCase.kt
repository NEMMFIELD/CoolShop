package com.example.coolshop.cart.domain

import com.example.database.models.CoolShopDBO
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor(private val repository: CartRepository) {
    suspend fun execute(product:CoolShopDBO) = repository.removeProductFromCart(product)
}
