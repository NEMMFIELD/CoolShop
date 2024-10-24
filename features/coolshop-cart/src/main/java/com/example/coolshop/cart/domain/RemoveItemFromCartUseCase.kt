package com.example.coolshop.cart.domain

import com.example.coolshop.cart.data.CartRepository
import com.example.database.models.CoolShopDBO
import javax.inject.Inject

internal class RemoveItemFromCartUseCase @Inject constructor(private val repository: CartRepository) {

    suspend fun execute(product: CoolShopDBO) = repository.removeProductFromCart(product)
}
