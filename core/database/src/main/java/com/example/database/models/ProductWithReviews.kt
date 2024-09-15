package com.example.database.models

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithReviews(
    @Embedded val coolShopDBO: CoolShopDBO,
    @Relation(
        parentColumn = "id",
        entityColumn = "product_id"
    ) val userReviewsDBO: List<UserReviewDBO>
)
