package com.example.coolshop.di

import android.content.Context
import com.example.database.CoolShopDatabase
import com.example.database.UserReviewsDatabase
import com.example.database.dao.CoolShopDao
import com.example.database.dao.UserReviewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideCoolShopDatabase(@ApplicationContext context: Context): CoolShopDatabase {
        return CoolShopDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserReviewsDatabase(@ApplicationContext context: Context): UserReviewsDatabase {
        return UserReviewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDao(database: CoolShopDatabase):CoolShopDao {
       return database.coolShopDao ?: error("Database reference is null")
    }

    @Provides
    @Singleton
    fun provideUserReviewsDao(database: UserReviewsDatabase) :UserReviewsDao {
        return database.userReviewsDao ?: error ("UserReviewsDatabase reference is null")
    }
}
