package com.example.coolshop.details.di

import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.details.data.CoolShopDetailsRepositoryImpl
import com.example.coolshop.details.data.CoolShopDetailsRepository
import com.example.database.dao.CoolShopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DetailsRepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryDetails(
        coolShopApi: CoolShopApi, dao: CoolShopDao
    ): CoolShopDetailsRepository = CoolShopDetailsRepositoryImpl(coolShopApi, dao)
}
