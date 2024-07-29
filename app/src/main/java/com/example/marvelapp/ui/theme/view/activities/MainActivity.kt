@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.marvelapp.ui.theme.view.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.marvelapp.model.data_source.dto.data.CharacterUiModel
import com.example.marvelapp.model.data_source.dto.repository.FavoriteCharacterRepository
import com.example.marvelapp.ui.theme.MarvelAppTheme
import com.example.marvelapp.ui.theme.view.pages.HeroDisplay
import com.example.marvelapp.ui.theme.viewModel.CharacterViewModel
import com.example.marvelapp.ui.theme.viewModel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val favoriteRepo = FavoriteCharacterRepository(this)
        characterViewModel =
            CharacterViewModel(MainViewModel.getCharacterRepository(), favoriteRepo)
        installSplashScreen()
        setContent {
            MarvelAppTheme {
                val loading by characterViewModel.isLoading.collectAsState(true)
                val error by characterViewModel.error.collectAsState(null)
                val charactersResult by characterViewModel.charactersResult.collectAsState(null)
                val favorites by characterViewModel.favorites.collectAsState()
                val isLoadingMore by characterViewModel.isLoadingMore.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    HeroDisplay(
                        { characterViewModel.getAllCharacters() },
                        { characterViewModel.getSpecificCharacter(it) },
                        { characterViewModel.toggleFavorite(it) },
                        { characterViewModel.loadNextCharacters() },
                        { context, character -> passCharacterDetailsToNewIntent(context, character) },
                        loading,
                        error,
                        charactersResult,
                        favorites,
                        isLoadingMore,
                    )


                }
            }
        }
    }

    fun passCharacterDetailsToNewIntent(context: Context, result: CharacterUiModel?) {
        val intent = Intent(this, HeroesActivity::class.java)

        intent.putExtra("stories", result?.stories?.map { it.name }.let { ArrayList(it) })
        intent.putExtra("events", result?.events?.map { it.name }.let { ArrayList(it) })
        intent.putExtra("comics", result?.comics?.map { it.name }.let { ArrayList(it) })
        startActivity(intent)
    }
}

