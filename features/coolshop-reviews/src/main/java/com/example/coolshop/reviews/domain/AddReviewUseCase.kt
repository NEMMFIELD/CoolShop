package com.example.coolshop.reviews.domain

import com.example.database.models.UserReviewDBO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddReviewUseCase @Inject constructor(private val repository: ReviewsRepository) {
    suspend fun execute(reviewDBO: UserReviewDBO)   {
       repository.addReview(reviewDBO)
    }
}
