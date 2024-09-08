package com.example.coolshop.cart.domain

import javax.inject.Inject

class ProductObserveUseCase @Inject constructor(repository: CartRepository) {
    val observedProduct = repository.flowCart
}
