package com.example.marvelapp.model.data_source.dto.repository

import com.example.marvelapp.api.remote.MarvelApi
import com.example.marvelapp.model.data_source.dto.CharactersDTO
import com.example.marvelapp.model.data_source.dto.Comics
import com.example.marvelapp.model.data_source.dto.Data
import com.example.marvelapp.model.data_source.dto.Events
import com.example.marvelapp.model.data_source.dto.Result
import com.example.marvelapp.model.data_source.dto.Series
import com.example.marvelapp.model.data_source.dto.Stories
import com.example.marvelapp.model.data_source.dto.Thumbnail
import com.example.marvelapp.model.data_source.dto.Url
import com.example.marvelapp.model.data_source.dto.data.ResultWrapper
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException

class CharacterRepositoryTest {
    @Mock
    private lateinit var mockMarvelApi: MarvelApi
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        characterRepository = CharacterRepository(mockMarvelApi)
    }


    @Test
    fun `callApiToGetCharacters returns data when API call is successful`() = runTest {

        val expectedCharacters = CharactersDTO(
            attributionHTML = "HTML",
            attributionText = "Text",
            code = 200,
            copyright = "Copyright",
            `data` = Data(
                count = 10,
                limit = 10,
                offset = 0,
                results = emptyList(),
                total = 100
            ),
            etag = "Etag",
            status = "Ok"
        )

        `when`(mockMarvelApi.getCharacters(offset = 0)).thenReturn(expectedCharacters)

        val result = characterRepository.callApiToGetCharacters() as ResultWrapper.Success
        assertEquals(0, result.value.size)
    }

    @Test
    fun `callApiForAllCharacters should return error on catch`() = runTest {
        `when`(mockMarvelApi.getCharacters(offset = 0)).thenThrow(HttpException::class.java)
        val result = characterRepository.callApiToGetCharacters()
        assertEquals(ResultWrapper.NetworkError, result)
    }

    @Test
    fun `callApiForSpecificCharacter returns characters on successful API call`() = runTest {
        val expectedCharacters = CharactersDTO(
            attributionHTML = "HTML",
            attributionText = "Text",
            code = 200,
            copyright = "Copyright",
            `data` = Data(
                count = 10,
                limit = 10,
                offset = 0,
                results = listOf(
                    Result(
                        comics = Comics(1, "", emptyList(), 1),
                        description = "Description",
                        events = Events(1, "", emptyList(), 1),
                        id = 123,
                        modified = "2024-04-11",
                        name = "Spider-Man",
                        resourceURI = "http://example.com/character1",
                        series = Series(1, "", emptyList(), 1),
                        stories = Stories(1, "", emptyList(), 1),
                        thumbnail = Thumbnail("", ""),
                        urls = listOf(Url("", ""))
                    )
                ),
                total = 100
            ),
            etag = "Etag",
            status = "Ok"
        )
        `when`(mockMarvelApi.getCharacterName(nameStartsWith = "Spider-Man")).thenReturn(
            expectedCharacters
        )

        val result =
            characterRepository.callApiForSpecificCharacter("Spider-Man") as ResultWrapper.Success

        assertEquals(1, result.value.size)
        assertEquals("Spider-Man", result.value.first().name)
        assertEquals(123, result.value.first().id)
        assertEquals(Events(1, "", emptyList(), 1).items, result.value.first().events)
        assertEquals(Comics(1, "", emptyList(), 1).items, result.value.first().comics)
        assertEquals(Stories(1, "", emptyList(), 1).items, result.value.first().stories)
        assertEquals(Thumbnail("","").path.plus("/landscape_incredible."),result.value.first().thumbnail)
    }

    @Test
    fun `callApiForSpecificCharacter should return error on catch`() = runTest {

        `when`(mockMarvelApi.getCharacterName(nameStartsWith = "Spider-Man")).thenThrow(
            RuntimeException("Error")
        )
        val result = characterRepository.callApiToGetCharacters()
        assertEquals(ResultWrapper.NetworkError, result)
    }


}



