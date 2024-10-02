package com.example.state

sealed class ApiState<out T> {
    data object Empty : ApiState<Nothing>()
    data class Success<out T>(val data: T) : ApiState<T>() {
        override fun equals(other: Any?): Boolean {
            return other is Success<*> && other.data == data
        }

        override fun hashCode(): Int {
            return data.hashCode()
        }
    }

    data class Failure(val message: Throwable) : ApiState<Nothing>()
}
