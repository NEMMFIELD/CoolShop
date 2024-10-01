package com.example.coolshop.reviews.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.reviews.domain.LoadUserReviewsUseCase
import com.example.data.UserReviewModel
import com.example.state.ApiState
import com.example.utils.Logger
import com.example.utils.Mapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowUserReviewsViewModel @Inject internal constructor(
    private val loadUserReviewsUseCase: LoadUserReviewsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val logger: Logger
) : ViewModel() {

    private val _reviewsStateFlow: MutableStateFlow<ApiState<List<UserReviewModel>>> =
        MutableStateFlow(ApiState.Empty)
    val reviewsStateFlow get() = _reviewsStateFlow

    val productId = savedStateHandle.get<String>(PRODUCT_ID)

    companion object {
        const val PRODUCT_ID = "id"
    }

    init {
        loadReviews(productId?.toInt() ?: 0)
    }

    internal fun loadReviews(productId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadUserReviewsUseCase.execute(productId).collect { reviews ->
                    if (reviews.isEmpty()) {
                        _reviewsStateFlow.value =
                            ApiState.Success(emptyList()) // Или другой подход, если необходимо
                        return@collect // Выход из collect, если данных нет
                    }

                    val reviewsList = reviews.map { elements ->
                        Mapper.mapReviewDBOToReview(elements)
                    }
                    _reviewsStateFlow.value = ApiState.Success(reviewsList)
                }
            } catch (e: Exception) {
               logger.e("ShowUserReviewsViewModel", "Error loading reviews", e)
                _reviewsStateFlow.value = ApiState.Failure(e)
            }
        }
    }
}

