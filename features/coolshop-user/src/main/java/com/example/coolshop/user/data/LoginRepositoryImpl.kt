package com.example.coolshop.user.data

import android.content.SharedPreferences
import com.example.coolshop.api.CoolShopApi
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.user.domain.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val api: CoolShopApi,private val sharedPreferences: SharedPreferences) : LoginRepository {
    override suspend fun login(loginRequest: LoginRequest): RegistrationResponse {
        return api.login(loginRequest)
    }

    override suspend fun saveToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }
}
