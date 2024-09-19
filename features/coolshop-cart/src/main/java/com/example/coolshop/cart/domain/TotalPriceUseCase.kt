package com.example.coolshop.cart.domain

import javax.inject.Inject

internal class TotalPriceUseCase @Inject  constructor(repository: CartRepository){
    val observedTotalPrice = repository.flowSum
}
