package com.example.coolshop.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.main.domain.CoolShopCategoryUseCase
import com.example.coolshop.main.domain.CoolShopFavouritesProductsUseCase
import com.example.coolshop.main.domain.CoolShopProductsUseCase
import com.example.coolshop.main.domain.LoadFavouritesProductsUseCase
import com.example.data.CoolShopModel
import com.example.state.ApiState
import com.example.utils.Logger
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
internal class CoolShopViewModel @Inject internal constructor(
    private val useCase: CoolShopProductsUseCase,
    private val useCaseFavourites: CoolShopFavouritesProductsUseCase,
    private val loadFavouritesProductsUseCase: LoadFavouritesProductsUseCase,
    private val categoryUseCase: CoolShopCategoryUseCase,
    private val logger: Logger
) : ViewModel() {
    private val _postStateFlow: MutableStateFlow<ApiState<List<CoolShopModel>>> =
        MutableStateFlow(ApiState.Empty)
    val postStateFlow: StateFlow<ApiState<List<CoolShopModel>>> = _postStateFlow
            .onStart { loadProducts() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                ApiState.Empty
            )


    internal fun loadProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    useCase.execute().collect { products ->
                        _postStateFlow.value = com.example.state.ApiState.Success(products)
                    }
                } catch (e: Exception) {
                    logger.e("CoolShopViewModel", "Error loading products", e)
                    _postStateFlow.value = com.example.state.ApiState.Failure(e)
                }
            }
        }
    }

    internal fun loadCategoryProducts(category: String) {
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
            logger.d("Error", "setFavoutires goes wrong")
        }
    }

    internal fun loadFavourites(coolShopModel: CoolShopModel) {
        runCatching {
            loadFavouritesProductsUseCase.execute(coolShopModel)
        }.onFailure {
            logger.d("Error", it.toString())
        }
    }
}
