package com.example.marvelapp.model.data_source.dto

data class Series(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>,
    val returned: Int
)