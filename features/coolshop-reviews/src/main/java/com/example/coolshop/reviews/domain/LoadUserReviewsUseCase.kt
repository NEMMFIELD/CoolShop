package com.example.coolshop.reviews.domain

import com.example.coolshop.reviews.data.UserReviewsRepository
import com.example.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class LoadUserReviewsUseCase @Inject constructor(private val repository: UserReviewsRepository) {
    fun execute(productId:Int?) = flow {
        val mList = repository.loadReviews(productId)
        emit(mList)
    }.flowOn(Dispatchers.IO)
}
