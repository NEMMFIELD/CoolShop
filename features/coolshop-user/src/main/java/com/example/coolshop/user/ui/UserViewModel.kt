package com.example.coolshop.user.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coolshop.api.models.LoginRequest
import com.example.coolshop.user.domain.LoginUseCase
import com.example.coolshop.user.domain.SaveTokenUseCase
import com.example.utils.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject internal constructor(
    private val useCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val logger: Logger
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.d("Error", "Coroutine exception: $exception")
    }

    private val _token = MutableStateFlow<String?>(null)
    val token: StateFlow<String?> get() = _token

    var account = LoginRequest("", "")

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch(exceptionHandler) {
            _token.value = useCase.execute(loginRequest = loginRequest).token
        }
    }

    fun saveToken(token: String) {
        viewModelScope.launch(exceptionHandler) {
            withContext(Dispatchers.IO) {
                saveTokenUseCase.execute(token)
            }
        }
    }
}
