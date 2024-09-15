package com.example.coolshop.reviews.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.reviews.domain.AddReviewUseCase
import com.example.data.ReviewModel
import com.example.database.models.UserReviewDBO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddReviewViewModel @Inject constructor(
    private val useCase: AddReviewUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _reviewModel = MutableLiveData<ReviewModel>()
    val reviewModel: LiveData<ReviewModel>
        get() = _reviewModel
    var id = 0
     val productId = savedStateHandle.get<String>("productId")

    fun addReview(reviewDBO: UserReviewDBO) {
        Log.d("ProductId", productId.toString())
        id++
        _reviewModel.value?.id = id
        viewModelScope.launch(Dispatchers.IO) { useCase.execute(reviewDBO) }
    }
}
