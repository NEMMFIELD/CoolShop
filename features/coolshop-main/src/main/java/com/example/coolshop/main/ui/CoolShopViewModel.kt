package com.example.coolshop.main.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.main.domain.CoolShopCategoryUseCase
import com.example.coolshop.main.domain.CoolShopFavouritesProductsUseCase
import com.example.coolshop.main.domain.CoolShopProductsUseCase
import com.example.coolshop.main.domain.LoadFavouritesProductsUseCase
import com.example.data.CoolShopModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class CoolShopViewModel @Inject internal constructor(
    private val useCase: CoolShopProductsUseCase,
    private val useCaseFavourites: CoolShopFavouritesProductsUseCase,
    private val loadFavouritesProductsUseCase: LoadFavouritesProductsUseCase,
    private val categoryUseCase: CoolShopCategoryUseCase,
) : ViewModel() {
    private val _postStateFlow: MutableStateFlow<com.example.state.ApiState<List<CoolShopModel>>> =
        MutableStateFlow(com.example.state.ApiState.Empty)
    val postStateFlow: StateFlow<com.example.state.ApiState<List<CoolShopModel>>>
        get() = _postStateFlow

    init {
        loadProducts()
    }

    internal fun loadProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    useCase.execute().collect { products ->
                        _postStateFlow.value = com.example.state.ApiState.Success(products)
                    }
                } catch (e: Exception) {
                    _postStateFlow.value = com.example.state.ApiState.Failure(e)
                }
            }
        }
    }

    internal fun loadCategoryProducts(category:String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    categoryUseCase.execute(category).collect { products ->
                        _postStateFlow.value = com.example.state.ApiState.Success(products)
                    }
                } catch (e: Exception) {
                    _postStateFlow.value = com.example.state.ApiState.Failure(e)
                }
            }
        }
    }

    internal fun setFavourites(coolShopModel: CoolShopModel) {
        runCatching {
            useCaseFavourites.execute(coolShopModel)
        }.onFailure {
            Log.d("Error", "setFavoutires goes wrong")
        }

    }

    internal fun loadFavourites(coolShopModel: CoolShopModel) {
        try {
            loadFavouritesProductsUseCase.execute(coolShopModel)
        }
        catch (e:Exception) {
            Log.d("Error",e.toString())
        }
    }

}
