package com.example.marvelapp.ui.theme.view.pages

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.marvelapp.R
import com.example.marvelapp.model.data_source.dto.data.CharacterUiModel
import com.example.marvelapp.ui.theme.MarvelAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroDisplay(
    getAllCharacters: () -> Unit,
    getSpecificCharacter: (String) -> Unit,
    toggleFavourite: (Int) -> Unit,
    loadNextCharacters: () -> Unit,
    passCharactersDetails: (Context, CharacterUiModel?) -> Unit,
    loading: Boolean,
    error: String?,
    charactersResult: List<CharacterUiModel>?,
    favorites: Set<Int>,
    isLoadingMore: Boolean,


    ) {
    var isSearching by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                ),
                title = {
                    Text("Marvel Heroes")
                },
                actions = {
                    if (isSearching) {
                        TextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                            },
                            modifier = Modifier.padding(end = 4.dp),
                            placeholder = {
                                Text("Search...")
                            }
                        )

                    } else {
                        IconButton(
                            onClick = { isSearching = true }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column {

                LinkList(
                    searchText,
                    getAllCharacters,
                    getSpecificCharacter,
                    passCharactersDetails,
                    loading,
                    error,
                    charactersResult,
                    toggleFavourite,
                    favorites,
                    loadNextCharacters,
                    isLoadingMore
                )


            }
        }
    }
}

@Composable
fun LinkList(
    searchText: String = "",
    getAllCharacters: () -> Unit,
    getSpecificCharacter: (String) -> Unit,
    passCharactersDetails: (Context, CharacterUiModel?) -> Unit,
    loading: Boolean,
    error: String?,
    charactersResult: List<CharacterUiModel>?,
    toggleFavourite: (Int) -> Unit,
    favorites: Set<Int>,
    loadNextCharacters: () -> Unit,
    isLoadingMore: Boolean
) {


    LaunchedEffect(searchText) {
        if (searchText.isEmpty()) {
            getAllCharacters.invoke()

        } else {
            getSpecificCharacter(searchText)
        }
    }



    when {
        loading -> LoadingUI()
        error != null -> ErrorUI(error)
        else -> {
            DisplayList(
                result = charactersResult,
                searchText = searchText,
                toggleFavourite = toggleFavourite,
                favorites = favorites,
                loadNextCharacters = loadNextCharacters,
                passCharactersDetails = passCharactersDetails,
                isLoadingMore = isLoadingMore
            )

        }
    }

}

@Composable
fun LoadingUI() {
    Log.d("trexei", "LoadingUI")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorUI(errorMessage: String) {
    Log.d("trexei", "ErrorUI")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(errorMessage, color = Color.Red)
    }
}

@Composable
fun DisplayList(

    result: List<CharacterUiModel>?,
    searchText: String,
    toggleFavourite: (Int) -> Unit,
    favorites: Set<Int>,
    loadNextCharacters: () -> Unit,
    passCharactersDetails: (Context, CharacterUiModel?) -> Unit,
    isLoadingMore: Boolean
) {
    Log.d("trexei", "DisplayList")

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        if (result != null) {
            items(result.size) { index ->
                CharacterItem(
                    characterUiModel = result[index],

                    toggleFavourite = toggleFavourite,
                    favorites = favorites,
                    passCharactersDetails = passCharactersDetails
                )

            }
        }
        if (searchText.isEmpty()) {
            item {
                LoadMoreButton(loadNextCharacters, isLoadingMore)
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCharacter() {
    MarvelAppTheme {
        CharacterItem(characterUiModel = CharacterUiModel(
            "Spider", 123, "", listOf(), listOf(),
            listOf()
        ), toggleFavourite = {}, favorites = setOf(1),{_,_->})

    }
}

@Composable
fun CharacterItem(
    characterUiModel: CharacterUiModel?,
    toggleFavourite: (Int) -> Unit,
    favorites: Set<Int>,
    passCharactersDetails: (Context, CharacterUiModel?) -> Unit


) {
    val context = LocalContext.current

    val isFavorite = favorites.contains(characterUiModel?.id)
    Box(

        Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .height(200.dp)
            .clickable {
                passCharactersDetails(context, characterUiModel)
            }
    ) {
        Card(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize()) {
                AsyncImage(
                    model = characterUiModel?.thumbnail,
                    contentDescription = "content",
                    modifier = Modifier.fillMaxSize(),
                    placeholder = painterResource(id = R.drawable.logo),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = characterUiModel!!.name,
                    color = Color.White,
                    fontSize = 30.sp,
                    modifier = Modifier.align(Alignment.BottomStart)
                )
                IconButton(
                    onClick = {
                        toggleFavourite(characterUiModel.id)

                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Yellow else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun LoadMoreButton(loadNextCharacters: () -> Unit, isLoadingMore: Boolean) {

    Button(
        onClick = {
            loadNextCharacters()
        },

        enabled = !isLoadingMore,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
            .padding(16.dp),
        content = {
            if (isLoadingMore) {
                CircularProgressIndicator()

            } else {
                Text("Load More")
            }
        }
    )
}
