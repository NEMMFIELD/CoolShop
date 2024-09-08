package com.example.coolshop.api

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.api.models.ResponseItem
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path

interface CoolShopApi {
    @GET("/products")
    suspend fun getAllProducts(): List<ResponseItem>

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id: String): ResponseItem

    @GET("/products/category/{name}")
    suspend fun getCategory(@Path("name") name: String): List<ResponseItem>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): RegistrationResponse
}

