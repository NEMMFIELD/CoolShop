package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.dao.UserReviewsDao
import com.example.database.models.CoolShopDBO
import com.example.database.models.UserReviewDBO

class UserReviewsDatabase internal constructor(private val database: UserReviewsRoomDatabase) {
    val userReviewsDao: UserReviewsDao
        get() = database.userReviewsDao()
}

@Database(entities = [UserReviewDBO::class], version = 2, exportSchema = false)
abstract class UserReviewsRoomDatabase : RoomDatabase() {
    abstract fun userReviewsDao(): UserReviewsDao
}

fun UserReviewsDatabase(applicationContext: Context): UserReviewsDatabase {
    val userReviewsDatabase = Room.databaseBuilder(
        checkNotNull(applicationContext.applicationContext),
        UserReviewsRoomDatabase::class.java,
        "reviews"
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
    return UserReviewsDatabase(userReviewsDatabase)
}

