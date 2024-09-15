package com.example.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "UserReviews")
class UserReviewDBO(
    @PrimaryKey(autoGenerate = true) val reviewId: Int?,
    @ColumnInfo("name") val userName: String?,
    @ColumnInfo("review") val review:String?,
    @ColumnInfo("product_id") val productId:Int?
)
