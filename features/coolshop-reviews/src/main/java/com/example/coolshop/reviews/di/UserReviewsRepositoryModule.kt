package com.example.coolshop.reviews.di

import com.example.coolshop.reviews.data.UserReviewsRepositoryImpl
import com.example.coolshop.reviews.domain.UserReviewsRepository
import com.example.database.dao.UserReviewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class UserReviewsRepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryAddingReview(
        dao: UserReviewsDao
    ): UserReviewsRepository = UserReviewsRepositoryImpl(dao)
}
