package com.example.coolshop.reviews.data

import android.util.Log
import com.example.coolshop.reviews.domain.UserReviewsRepository
import com.example.database.dao.UserReviewsDao
import com.example.database.models.UserReviewDBO
import javax.inject.Inject

internal class UserReviewsRepositoryImpl @Inject constructor(private val dao: UserReviewsDao?):
    UserReviewsRepository {
    override suspend fun addReview(review: UserReviewDBO) {
        dao?.insert(review) ?: Log.d("CoolShopReviews", "CoolShopReviews reference is null")
    }

    override suspend fun loadReviews(productId: Int?): List<UserReviewDBO>? {
        return dao?.getReviewByProduct(productId)
    }
}
