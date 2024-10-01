package com.example.coolshop.user.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.user.domain.LoginUseCase
import com.example.coolshop.user.domain.SaveTokenUseCase
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject internal constructor(
    private val useCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val logger:Logger
) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String> get() = _token
    var account = LoginRequest("", "")
    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            try {
                _token.value = useCase.execute(loginRequest = loginRequest).token
            } catch (e: Exception) {
                logger.d("Error", "Error from login:${e}")
            }
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    saveTokenUseCase.execute(token)
                }
               catch (e: Exception) {
                   logger.d("Error", "Error from save token:${e}")
               }
            }
        }
    }
}
