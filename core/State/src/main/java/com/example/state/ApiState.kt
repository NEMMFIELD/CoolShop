package com.example.state

 sealed class ApiState<out T> {
    class Failure(val message:Throwable): ApiState<Nothing>()
    class Success<T>(val data:T): ApiState<T>()
    data object Empty : ApiState<Nothing>()
}
