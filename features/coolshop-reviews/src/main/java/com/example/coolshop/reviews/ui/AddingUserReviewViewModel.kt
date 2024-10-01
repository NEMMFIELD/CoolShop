package com.example.coolshop.reviews.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.reviews.domain.AddUserReviewUseCase
import com.example.data.UserReviewModel
import com.example.database.models.UserReviewDBO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddingUserReviewViewModel @Inject internal constructor(
    private val useCase: AddUserReviewUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userReviewModel = MutableLiveData<UserReviewModel>()
    val userReviewModel: LiveData<UserReviewModel>
        get() = _userReviewModel
    var id = 0
    val productId = savedStateHandle.get<String>("productId")

    fun addReview(reviewDBO: UserReviewDBO) {
        id++
        _userReviewModel.value?.id = id
        viewModelScope.launch(Dispatchers.IO) { useCase.execute(reviewDBO) }
    }
}
