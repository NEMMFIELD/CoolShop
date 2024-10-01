package com.example.coolshop.di

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.cart.data.CartRepositoryImpl
import com.example.coolshop.cart.domain.CartRepository
import com.example.coolshop.details.data.CoolShopDetailsRepositoryImpl
import com.example.coolshop.details.domain.CoolShopDetailsRepository
import com.example.coolshop.main.data.models.CoolShopRepositoryImpl
import com.example.coolshop.main.domain.CoolShopRepository
import com.example.coolshop.reviews.data.UserReviewsRepositoryImpl
import com.example.coolshop.reviews.domain.UserReviewsRepository
import com.example.coolshop.user.data.LoginRepositoryImpl
import com.example.coolshop.user.domain.LoginRepository
import com.example.database.dao.CoolShopDao
import com.example.database.dao.UserReviewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {
    @Provides
    @Singleton
    fun provideRepository(
        coolShopApi: CoolShopApi, sharedPreferences: SharedPreferences
    ): CoolShopRepository = CoolShopRepositoryImpl(coolShopApi, sharedPreferences)

    @Provides
    @Singleton
    fun provideRepositoryDetails(
        coolShopApi: CoolShopApi, dao: CoolShopDao
    ): CoolShopDetailsRepository = CoolShopDetailsRepositoryImpl(coolShopApi, dao)

    @Provides
    @Singleton
    fun provideRepositoryCart(dao: CoolShopDao): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideRepositoryLogin(
        api: CoolShopApi,
        sharedPreferences: SharedPreferences
    ): LoginRepository = LoginRepositoryImpl(api, sharedPreferences)

    @Provides
    @Singleton
    fun provideRepositoryAddingReview(
        dao: UserReviewsDao
    ): UserReviewsRepository = UserReviewsRepositoryImpl(dao)
}
