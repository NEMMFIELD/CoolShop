package com.example.coolshop.details.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.details.domain.AddingToCartUseCase
import com.example.coolshop.details.domain.CoolShopDetailsUseCase
import com.example.data.CoolShopModel
import com.example.database.models.CoolShopDBO
import com.example.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val ID = "id"
    }

    val id = savedStateHandle.get<String>(ID)

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        _postProduct.value = State.Failure(exception)
    }

    private val _postProduct: MutableStateFlow<State<CoolShopModel>> =
        MutableStateFlow(State.Empty)

    val postProduct: StateFlow<State<CoolShopModel>> = _postProduct
        .onStart { loadSelectedProduct(id.toString()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State.Empty
        )

    fun loadSelectedProduct(id: String) {

        viewModelScope.launch(exceptionHandler) {
            useCase.execute(id).collect { product ->
                _postProduct.value = State.Success(product)
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
