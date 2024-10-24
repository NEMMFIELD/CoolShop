package com.example.coolshop.reviews.data

import com.example.data.UserReviewModel
import com.example.database.models.UserReviewDBO

const val UNKNOW_USERNAME = "Unknown"
const val UNKNOW_REVIEW = "Unknown"
class CoolShopReviewsMapper {
    companion object {

        fun mapReviewDBOToReview(dbo: UserReviewDBO): UserReviewModel {
            return UserReviewModel(
                id = dbo.reviewId ?: 0,
                user = dbo.userName ?: UNKNOW_USERNAME,
                review = dbo.review ?: UNKNOW_REVIEW,
                productId = dbo.productId ?: 0
            )
        }


        fun mapReviewModelToReviewDbo(review: UserReviewModel): UserReviewDBO = UserReviewDBO(
            userName = review.user,
            review = review.review,
            reviewId = review.id,
            productId = review.productId
        )
    }
}


