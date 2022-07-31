package com.appiadev.model.api

import java.lang.Exception

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success [data = $data]"
            is Error -> "Error [error $exception"
        }
    }
}

val Result<*>.Succeeded
    get() = this is Result.Success && data != null