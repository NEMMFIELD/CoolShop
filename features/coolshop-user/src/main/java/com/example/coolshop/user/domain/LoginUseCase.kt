package com.example.coolshop.user.domain

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun execute(loginRequest: LoginRequest):RegistrationResponse {
       return repository.login(loginRequest)
    }
}
