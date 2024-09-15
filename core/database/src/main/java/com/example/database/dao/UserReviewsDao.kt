package com.example.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.models.UserReviewDBO

@Dao
interface UserReviewsDao {
    @Query("SELECT * FROM UserReviews WHERE product_id = :productId")
    suspend fun getReviewByProduct(productId:Int?): List<UserReviewDBO>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: UserReviewDBO)

    @Delete
    suspend fun remove(review: UserReviewDBO)

    @Query("DELETE FROM UserReviews")
    suspend fun clean()
}
