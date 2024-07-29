package com.example.marvelapp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.model.data_source.dto.data.CharacterUiModel
import com.example.marvelapp.model.data_source.dto.repository.CharacterRepository
import com.example.marvelapp.model.data_source.dto.repository.FavoriteCharacterRepository
import com.example.marvelapp.model.data_source.dto.data.ResultWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val characterRepository: CharacterRepository,
    private val favoriteCharacterRepository: FavoriteCharacterRepository? = null
) : ViewModel() {

    private val _charactersResult = MutableStateFlow<List<CharacterUiModel>?>(null)
    val charactersResult: StateFlow<List<CharacterUiModel>?> = _charactersResult

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    private var displayedHeroesCount = 0


    val favorites: StateFlow<Set<Int>> =
        favoriteCharacterRepository?.favorites as? StateFlow<Set<Int>>
            ?: MutableStateFlow(emptySet())


    init {
        viewModelScope.launch {
            favoriteCharacterRepository?.loadFavorites()
        }
    }

    fun toggleFavorite(characterId: Int) {

        viewModelScope.launch {
            favoriteCharacterRepository?.toggleFavorite(characterId)
        }
    }


    fun getAllCharacters() {
        _isLoading.value = true

        viewModelScope.launch {
            val response = characterRepository.callApiToGetCharacters()
            when (response) {
                ResultWrapper.NetworkError -> {
                    _error.value = "Failed to get characters"
                }

                is ResultWrapper.Success -> _charactersResult.value = response.value
            }

            _isLoading.value = false

        }
    }

    fun getSpecificCharacter(name: String) {

        _isLoading.value = true

        viewModelScope.launch {
            val response = characterRepository.callApiForSpecificCharacter(name)

            when (response) {
                ResultWrapper.NetworkError -> _error.value = "Failed to get character"
                is ResultWrapper.Success -> _charactersResult.value = response.value
            }

            _isLoading.value = false
        }
    }

    fun loadNextCharacters() {
        _isLoadingMore.value = true
        viewModelScope.launch {
            displayedHeroesCount += 10
            val response = characterRepository.callApiToGetCharacters(displayedHeroesCount)
            when (response) {
                ResultWrapper.NetworkError -> _error.value = "Failed to get characters"
                is ResultWrapper.Success -> _charactersResult.value =
                    _charactersResult.value?.plus(response.value)
            }
            _isLoadingMore.value = false
        }
    }


}
