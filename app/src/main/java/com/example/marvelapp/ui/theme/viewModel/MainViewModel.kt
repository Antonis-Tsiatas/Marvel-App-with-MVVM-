package com.example.marvelapp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import com.example.marvelapp.api.remote.ApiClient
import com.example.marvelapp.api.remote.MarvelApi
import com.example.marvelapp.model.data_source.dto.repository.CharacterRepository

class MainViewModel : ViewModel() {

    companion object {
        val marvelApi: MarvelApi = ApiClient.apiService
        fun getCharacterRepository(): CharacterRepository {
            return CharacterRepository(marvelApi)
        }
    }


}