package com.example.coolshop.api

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.api.models.ProductDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CoolShopApi {
    @GET("/products")
    suspend fun getAllProducts(): List<ProductDTO>

    @GET("/products/{id}")
    suspend fun getProduct(@Path("id") id: String): ProductDTO

    @GET("/products/category/{name}")
    suspend fun getCategory(@Path("name") name: String): List<ProductDTO>

    @POST("/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): RegistrationResponse
}

