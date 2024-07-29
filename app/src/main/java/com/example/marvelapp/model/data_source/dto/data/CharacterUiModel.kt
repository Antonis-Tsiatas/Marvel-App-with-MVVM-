package com.example.marvelapp.model.data_source.dto.data

import com.example.marvelapp.model.data_source.dto.Item
import com.example.marvelapp.model.data_source.dto.ItemXXX

data class CharacterUiModel(
    val name: String,
    val id: Int,
    val thumbnail: String,
    val comics: List<Item>,
    val events: List<Item>,
    val stories: List<ItemXXX>
)
