package com.example.marvelapp.model.data_source.dto.repository

import com.example.marvelapp.api.remote.MarvelApi
import com.example.marvelapp.model.data_source.dto.CharactersDTO
import com.example.marvelapp.model.data_source.dto.data.CharacterUiModel
import com.example.marvelapp.model.data_source.dto.data.ResultWrapper

class CharacterRepository(private val marvelApi: MarvelApi) {

    suspend fun callApiToGetCharacters(offset: Int=0): ResultWrapper<List<CharacterUiModel>> {
        return try {
            val response = marvelApi.getCharacters(offset = offset)


            ResultWrapper.Success(mapToCharacterUiModel(response))
        } catch (e: Exception) {
            ResultWrapper.NetworkError
        }

    }

    suspend fun callApiForSpecificCharacter(name: String): ResultWrapper<List<CharacterUiModel>> {
        return try {

            val response = marvelApi.getCharacterName(nameStartsWith = name)
            ResultWrapper.Success(mapToCharacterUiModel(response))
        } catch (e: Exception) {
            ResultWrapper.NetworkError
        }
    }




    private fun mapToCharacterUiModel(response: CharactersDTO): List<CharacterUiModel> {
        return response.data.results.map {
            CharacterUiModel(
                name = it.name,
                id = it.id,
                thumbnail = it.thumbnail.path.replace("http", "https")
                    .plus("/landscape_incredible.".plus(it.thumbnail.extension)),
                comics = it.comics.items,
                events = it.events.items,
                stories = it.stories.items
            )
        }
    }
}


