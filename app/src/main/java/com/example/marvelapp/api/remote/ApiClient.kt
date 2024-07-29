package com.example.marvelapp.api.remote

object ApiClient {
    val apiService: MarvelApi by lazy {
        RetrofitClient.retrofit.create(MarvelApi::class.java)
    }}