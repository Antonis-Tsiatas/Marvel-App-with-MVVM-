package com.example.marvelapp.model.data_source.dto.data

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    object NetworkError : ResultWrapper<Nothing>()
}