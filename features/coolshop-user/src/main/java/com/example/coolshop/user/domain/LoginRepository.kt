package com.example.coolshop.user.domain

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse

internal interface LoginRepository {
   suspend fun login(loginRequest: LoginRequest):RegistrationResponse
   suspend fun saveToken(token:String)
}
