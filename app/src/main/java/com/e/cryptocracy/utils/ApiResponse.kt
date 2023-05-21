package com.e.cryptocracy.utils

sealed class ApiResponse {
    data class Success<T>(val data: T) : ApiResponse()
    data class Failed<T>(val data: T?, val errorMsg: String, val ex: Exception? = null) :
        ApiResponse()
}
