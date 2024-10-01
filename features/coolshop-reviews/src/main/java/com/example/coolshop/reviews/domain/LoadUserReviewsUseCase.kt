package com.example.coolshop.reviews.domain

import android.util.Log
import com.example.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

internal class LoadUserReviewsUseCase @Inject constructor(private val repository: UserReviewsRepository,private val logger: Logger) {
    fun execute(productId:Int?) = flow {
        val mList = repository.loadReviews(productId)
        logger.d("From interactor", mList.size.toString())
        emit(mList)
    }.flowOn(Dispatchers.IO)
}
