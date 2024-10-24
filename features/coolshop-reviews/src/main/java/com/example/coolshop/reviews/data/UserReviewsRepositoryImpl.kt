package com.example.coolshop.reviews.data

import com.example.database.dao.UserReviewsDao
import com.example.database.models.UserReviewDBO
import javax.inject.Inject

class UserReviewsRepositoryImpl @Inject constructor(private val dao: UserReviewsDao):
    UserReviewsRepository {
    override suspend fun addReview(review: UserReviewDBO) {
        dao.insert(review)
    }

    override suspend fun loadReviews(productId: Int?):List<UserReviewDBO> {
        return dao.getReviewByProduct(productId)
    }
}
