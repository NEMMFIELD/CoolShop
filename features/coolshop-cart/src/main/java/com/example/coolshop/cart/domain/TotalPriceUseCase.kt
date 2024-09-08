package com.example.coolshop.cart.domain

import javax.inject.Inject

class TotalPriceUseCase @Inject  constructor(repository: CartRepository){
    val observedTotalPrice = repository.flowSum
}
