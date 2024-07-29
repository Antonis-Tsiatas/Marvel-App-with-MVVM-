package com.example.marvelapp.model.data_source.dto.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_heroes")

class FavoriteCharacterRepository(private val context: Context) {
    private val FAVORITE_KEY = stringPreferencesKey("favorite_heroes")

    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())

    val favorites: StateFlow<Set<Int>> = _favorites.asStateFlow()



     suspend fun loadFavorites() {
        context.dataStore.data.map { preferences ->
            preferences[FAVORITE_KEY]?.split(",")?.mapNotNull(String::toIntOrNull)?.toSet() ?: emptySet()
        }.collect { favoritesSet ->
            _favorites.value = favoritesSet
        }
    }

    suspend fun toggleFavorite(characterId: Int) {
        context.dataStore.edit { preferences ->
            val currentFavoritesString = preferences[FAVORITE_KEY] ?: ""
            val currentFavorites = currentFavoritesString.split(",").mapNotNull(String::toIntOrNull).toMutableSet()

            if (currentFavorites.contains(characterId)) {
                currentFavorites.remove(characterId)
                Log.d("FavoriteRepository", "Removed $characterId from favorites")
            } else {
                currentFavorites.add(characterId)
                Log.d("FavoriteRepository", "Added $characterId to favorites")
            }
            preferences[FAVORITE_KEY] = currentFavorites.joinToString(",")
            _favorites.value = currentFavorites
        }
    }
}