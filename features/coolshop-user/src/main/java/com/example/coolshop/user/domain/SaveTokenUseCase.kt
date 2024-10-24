package com.example.coolshop.user.domain

import javax.inject.Inject

internal class SaveTokenUseCase @Inject constructor(private val repository: LoginRepository) {
   suspend fun execute(token:String) {
        repository.saveToken(token)
    }
}
