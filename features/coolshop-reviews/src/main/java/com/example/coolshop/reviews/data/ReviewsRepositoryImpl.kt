package com.example.coolshop.reviews.data

import com.example.coolshop.reviews.domain.ReviewsRepository
import com.example.database.dao.UserReviewsDao
import com.example.database.models.UserReviewDBO
import com.example.utils.Mapper
import javax.inject.Inject

class ReviewsRepositoryImpl @Inject constructor(private val dao: UserReviewsDao):ReviewsRepository {
    override suspend fun addReview(review: UserReviewDBO) {
        dao.insert(review)
    }

    override suspend fun loadReviews(productId: Int?):List<UserReviewDBO> {
        return dao.getReviewByProduct(productId)
    }
}
