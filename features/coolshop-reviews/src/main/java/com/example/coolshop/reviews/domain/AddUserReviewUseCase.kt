package com.example.coolshop.reviews.domain

import com.example.database.models.UserReviewDBO
import javax.inject.Inject

internal class AddUserReviewUseCase @Inject constructor(private val repository: UserReviewsRepository) {
    suspend fun execute(reviewDBO: UserReviewDBO)   {
       repository.addReview(reviewDBO)
    }
}
