package com.example.coolshop.user.di

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.user.data.LoginRepositoryImpl
import com.example.coolshop.user.data.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LoginRepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryLogin(
        api: CoolShopApi,
        sharedPreferences: SharedPreferences
    ): LoginRepository = LoginRepositoryImpl(api, sharedPreferences)
}
