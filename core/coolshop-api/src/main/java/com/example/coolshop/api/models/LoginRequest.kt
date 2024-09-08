package com.example.coolshop.api.models

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name ="username")var username: String,
    @Json(name = "password")var password: String
)
