package com.example.utils

import com.example.coolshop.api.models.ResponseItem
import com.example.data.CoolShopModel
import com.example.data.UserReviewModel
import com.example.database.models.CoolShopDBO
import com.example.database.models.UserReviewDBO

class Mapper {
    companion object {
        fun mapModelToDBO(
            shopModel: CoolShopModel,

            ): CoolShopDBO {
            return CoolShopDBO(
                id = shopModel.id,
                title = shopModel.title,
                imgPath = shopModel.imgPath,
                price = shopModel.price,
                rate = shopModel.rate,
                isLiked = shopModel.isLiked,
                description = shopModel.description,
                category = shopModel.category
            )
        }

        fun mapModelDBOToModel(shopDBO: CoolShopDBO): CoolShopModel = CoolShopModel(
            id = shopDBO.id,
            title = shopDBO.title,
            imgPath = shopDBO.imgPath,
            price = shopDBO.price,
            rate = shopDBO.rate,
            description = shopDBO.description,
            category = shopDBO.category,
            isLiked = shopDBO.isLiked
        )

        fun mapDTOToModel(
            shopDTO: ResponseItem,
            ): CoolShopModel {
            return CoolShopModel(
                id = shopDTO.id,
                title = shopDTO.title,
                imgPath = shopDTO.image,
                price = shopDTO.price,
                rate = shopDTO.rating?.rate,
                isLiked = false,
                description = shopDTO.description,
                category = shopDTO.category
            )
        }

        fun mapReviewDBOToReview(reviewDBO: UserReviewDBO): UserReviewModel = UserReviewModel(
            id = reviewDBO.reviewId,
            user = reviewDBO.userName,
            review = reviewDBO.review,
            productId = reviewDBO.productId
        )

        fun mapReviewModelToReviewDbo(review: UserReviewModel): UserReviewDBO = UserReviewDBO(
            userName = review.user,
            review = review.review,
            reviewId = review.id,
            productId = review.productId
        )
    }
}
