package com.example.coolshop.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.details.domain.AddingToCartUseCase
import com.example.coolshop.details.domain.CoolShopDetailsUseCase
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import com.example.state.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CoolShopDetailsViewModel @Inject internal constructor(
    private val useCase: CoolShopDetailsUseCase,
    private val addingToCartUseCase: AddingToCartUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _postStateFlow: MutableStateFlow<ApiState<CoolShopModel>> =
        MutableStateFlow(ApiState.Empty)
    val postStateFlow: StateFlow<ApiState<CoolShopModel>> = _postStateFlow
        .onStart { loadSelectedProduct(id.toString()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            ApiState.Empty
        )
    val id = savedStateHandle.get<String>(ID)

    companion object {
        const val ID = "id"
    }

    fun loadSelectedProduct(id: String) {
        viewModelScope.launch {
            try {
                useCase.execute(id).collect { product ->
                    _postStateFlow.value = ApiState.Success(product)
                }
            } catch (e: Exception) {
                _postStateFlow.value = ApiState.Failure(e)
            }
        }
    }

    fun addToCardProduct(coolShopDBO: CoolShopDBO) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                addingToCartUseCase.execute(coolShopDBO)
            }
        }
    }
}
