package com.example.coolshop.cart.domain

import com.example.coolshop.cart.data.CartRepository
import javax.inject.Inject

internal class ProductObserveUseCase @Inject  constructor(repository: CartRepository) {
    val observedProductsInCart = repository.flowCart
}
