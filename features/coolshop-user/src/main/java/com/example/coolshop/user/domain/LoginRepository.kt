package com.example.coolshop.user.domain

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse

interface LoginRepository {
   suspend fun login(loginRequest: LoginRequest):RegistrationResponse
   suspend fun saveToken(token:String)
}
