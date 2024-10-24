package com.example.coolshop.cart.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.cart.domain.ProductObserveUseCase
import com.example.coolshop.cart.domain.RemoveItemFromCartUseCase
import com.example.coolshop.cart.domain.TotalPriceUseCase
import com.example.database.models.CoolShopDBO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class CartViewModel @Inject constructor(
    private val removeItem: RemoveItemFromCartUseCase,
    observedProductObserveUseCase: ProductObserveUseCase,
    totalPriceUseCase: TotalPriceUseCase,
) : ViewModel() {

    var productsInCart: StateFlow<List<CoolShopDBO>> =
        observedProductObserveUseCase.observedProductsInCart.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    var totalPrice: StateFlow<Double?> = totalPriceUseCase.observedTotalPriceInCart.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun removeItem(product: CoolShopDBO) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                removeItem.execute(product)
            }
        }
    }
}
