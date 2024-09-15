package com.example.coolshop.reviews.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoadUserReviewsUseCase @Inject constructor(private val repository: ReviewsRepository) {
    fun execute(productId:Int?) = flow {
        val mList = repository.loadReviews(productId)
        Log.d("From interactor", mList.size.toString())
        emit(mList)
    }.flowOn(Dispatchers.IO)
}
