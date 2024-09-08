package com.example.coolshop.cart.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.coolshop.cart.domain.LoadCartUseCase
import com.example.coolshop.cart.domain.RemoveItemFromCartUseCase
import com.example.coolshop.cart.domain.ProductObserveUseCase
import com.example.coolshop.cart.domain.TotalPriceUseCase
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class CartViewModel @Inject constructor(
    private val removeItem: RemoveItemFromCartUseCase,
    private val observedProductObserveUseCase: ProductObserveUseCase,
    private val totalPriceUseCase: TotalPriceUseCase,
) : ViewModel() {
    var cartedProducts: LiveData<List<CoolShopDBO>> =
        observedProductObserveUseCase.observedProduct.asLiveData()
    var totalPrice:LiveData<Double?> = totalPriceUseCase.observedTotalPrice.asLiveData()

    fun removeItem(product: CoolShopDBO) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                removeItem.execute(product)
            }
        }
    }
}
