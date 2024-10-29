package com.example.coolshop.reviews.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.reviews.data.CoolShopReviewsMapper
import com.example.coolshop.reviews.domain.LoadUserReviewsUseCase
import com.example.data.UserReviewModel
import com.example.state.State
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PRODUCT_ID = "id"

@HiltViewModel
class ShowUserReviewsViewModel @Inject internal constructor(
    private val loadUserReviewsUseCase: LoadUserReviewsUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val logger: Logger
) : ViewModel() {

    private val _reviewsStateFlow: MutableStateFlow<State<List<UserReviewModel>>> =
        MutableStateFlow(State.Empty)

    val reviewsStateFlow
        get() = _reviewsStateFlow
            .onStart { loadReviews(productId?.toInt()) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                State.Empty
            )

    val productId = savedStateHandle.get<String>(PRODUCT_ID)

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.e("ShowUserReviewsViewModel", "Error loading reviews", exception)
        _reviewsStateFlow.value = State.Failure(exception)
    }

    internal fun loadReviews(productId: Int?) {

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            loadUserReviewsUseCase.execute(productId).collect { reviews ->
                if (reviews.isNullOrEmpty()) {
                    _reviewsStateFlow.value =
                        State.Success(emptyList()) // Или другой подход, если необходимо
                    return@collect // Выход из collect, если данных нет
                }

                val reviewsList = reviews.map { elements ->
                    CoolShopReviewsMapper.mapReviewDBOToReview(elements)
                }
                _reviewsStateFlow.value = State.Success(reviewsList)
            }
        }
    }
}

