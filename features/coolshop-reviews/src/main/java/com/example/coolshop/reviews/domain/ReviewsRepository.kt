package com.example.coolshop.reviews.domain

import com.example.database.models.UserReviewDBO

interface ReviewsRepository {
   suspend fun addReview(review: UserReviewDBO)
   suspend fun loadReviews(productId:Int?):List<UserReviewDBO>
}
