package com.example.coolshop.api.models

import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponse(
    @Json(name = "token") val token: String
)
