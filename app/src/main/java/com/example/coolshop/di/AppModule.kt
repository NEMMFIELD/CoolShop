package com.example.coolshop.di

import android.content.Context
import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.cart.data.CartRepositoryImpl
import com.example.coolshop.cart.domain.CartRepository
import com.example.coolshop.details.data.CoolShopDetailsRepositoryImpl
import com.example.coolshop.details.domain.CoolShopDetailsRepository
import com.example.coolshop.main.data.models.CoolShopRepositoryImpl
import com.example.coolshop.main.domain.CoolShopRepository
import com.example.coolshop.reviews.data.UserUserReviewsRepositoryImpl
import com.example.coolshop.reviews.domain.UserReviewsRepository
import com.example.coolshop.user.data.LoginRepositoryImpl
import com.example.coolshop.user.domain.LoginRepository
import com.example.database.CoolShopDatabase
import com.example.database.UserReviewsDatabase
import com.example.database.dao.CoolShopDao
import com.example.database.dao.UserReviewsDao
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


const val SHARED_PREF_NAME = "MyPreference"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://fakestoreapi.com"

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun httpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    "Bearer ${sharedPreferences.getString("token", "0").orEmpty()}"
                ).build()
                return@addInterceptor chain.proceed(request)
            }
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): CoolShopApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()
        .create(CoolShopApi::class.java)


    @Provides
    @Singleton
    fun provideRepository(
        coolShopApi: CoolShopApi, sharedPreferences: SharedPreferences
    ): CoolShopRepository = CoolShopRepositoryImpl(coolShopApi, sharedPreferences)

    @Provides
    @Singleton
    fun provideCoolShopDatabase(@ApplicationContext context: Context): CoolShopDatabase {
        return CoolShopDatabase(context)
    }

    @Provides
    @Singleton
    fun provideUserReviewsDatabase(@ApplicationContext context:Context):UserReviewsDatabase {
        return UserReviewsDatabase(context)
    }

    @Provides
    @Singleton
    fun provideDao(database: CoolShopDatabase) = database.coolShopDao

    @Provides
    @Singleton
    fun provideUserReviewsDao(database: UserReviewsDatabase) = database.userReviewsDao

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
    ): UserReviewsRepository = UserUserReviewsRepositoryImpl(dao)

}
