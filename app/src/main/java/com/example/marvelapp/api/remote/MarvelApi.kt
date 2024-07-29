package com.example.marvelapp.api.remote


import com.example.marvelapp.model.data_source.dto.CharactersDTO
import com.example.marvelapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("apikey") apikey:String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timeStamp,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: Int = 10,
        @Query("offset") offset:Int

    ): CharactersDTO

    @GET("/v1/public/characters")
    suspend fun getCharacterName(
        @Query("apikey") apikey:String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timeStamp,
        @Query("hash") hash: String = Constants.hash(),
        @Query("nameStartsWith") nameStartsWith: String

    ): CharactersDTO
    @GET("/v1/public/characters/{characterId}/comics")
    suspend fun getCharacterID(
        @Query("apikey") apikey:String = Constants.API_KEY,
        @Query("ts") ts: String = Constants.timeStamp,
        @Query("hash") hash: String = Constants.hash(),
        @Query("limit") limit: Int = 10,
        @Query("offset") offset:Int

    ): CharactersDTO
}