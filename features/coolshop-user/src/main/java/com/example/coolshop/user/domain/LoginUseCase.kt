package com.example.coolshop.user.domain

import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.api.models.RegistrationResponse
import com.example.coolshop.user.data.LoginRepository
import javax.inject.Inject

internal class LoginUseCase @Inject constructor(private val repository: LoginRepository) {
    suspend fun execute(loginRequest: LoginRequest):RegistrationResponse {
       return repository.login(loginRequest)
    }
}
