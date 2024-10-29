package com.example.coolshop.reviews.domain

import com.example.database.models.UserReviewDBO

internal interface UserReviewsRepository {
   suspend fun addReview(review: UserReviewDBO)
   suspend fun loadReviews(productId:Int?):List<UserReviewDBO>?
}
