package com.example.coolshop.main.di

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.main.data.CoolShopRepositoryImpl
import com.example.coolshop.main.domain.CoolShopRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CoolShopRepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        coolShopApi: CoolShopApi, sharedPreferences: SharedPreferences
    ): CoolShopRepository = CoolShopRepositoryImpl(coolShopApi, sharedPreferences)
}
