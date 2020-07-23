package com.mohsen.apk.wetterkleidung.model

import java.lang.Exception

sealed class RepositoryResponse<out T> {
    class Success<T>(val data: T) : RepositoryResponse<T>()
    class Filure(val exception: Exception) : RepositoryResponse<Nothing>()
}