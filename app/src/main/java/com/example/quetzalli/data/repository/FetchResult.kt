package com.example.quetzalli.data.repository

sealed class FetchResult <out T>{
    data class Success<out T>(val data: T): FetchResult<T>()
    data class Error(val exception: Exception): FetchResult<Nothing>()
}