package com.c0d3in3.btuclassroom.data.remote.model

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception?, val message: String? = null) : Result<Nothing>()
}