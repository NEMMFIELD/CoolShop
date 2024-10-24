package com.example.coolshop.cart.di

import com.example.coolshop.cart.data.CartRepositoryImpl
import com.example.coolshop.cart.data.CartRepository
import com.example.database.dao.CoolShopDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartRepositoryModule {
    @Provides
    @Singleton
    fun provideRepositoryCart(dao: CoolShopDao): CartRepository = CartRepositoryImpl(dao)
}
