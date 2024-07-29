package com.example.marvelapp.ui.theme.view.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.marvelapp.ui.theme.MarvelAppTheme

@Composable
fun GetContent(
    stories: ArrayList<String>,
    events: ArrayList<String>,
    comics: ArrayList<String>,
    onToggleStoriesClick: (MutableState<Boolean>)->Unit,
    onToggleEventsClick: (MutableState<Boolean>)->Unit,
    onToggleComicsClick: (MutableState<Boolean>)->Unit
) {
    MarvelAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            Column {
                HeroesDetails(
                    stories ,
                    events,
                    comics,
                    onToggleStoriesClick,
                    onToggleEventsClick,
                    onToggleComicsClick
                )

            }
        }
    }

}

@Composable
fun HeroesDetails(
    textStories: List<String>,
    textEvents: List<String>,
    textComics: List<String>,
    onToggleStoriesClick: (MutableState<Boolean>)->Unit,
    onToggleEventsClick: (MutableState<Boolean>)->Unit,
    onToggleComicsClick: (MutableState<Boolean>)->Unit,

) {
    val showStories = remember { mutableStateOf(false) }
    val showEvents = remember { mutableStateOf(false) }
    val showComics = remember { mutableStateOf(false) }
    Column {
        TextButton(onClick = { onToggleStoriesClick.invoke(showStories)}) {
            Text(
                //check
                text = if (showStories.value){"Hide Stories"}else{"Show Stories"})
        }
        //heroesViewModel.getButtonTitle(showStories,"stories"))
        GetDescription(textIsShown = showStories, textValue = textStories)

        TextButton(onClick = { onToggleEventsClick.invoke(showEvents) }) {

            Text(

                text = if(showEvents.value){"Hide Events"}else{"Show Events"})
        }
        GetDescription(textIsShown = showEvents, textValue = textEvents)

        TextButton(onClick = { onToggleComicsClick.invoke(showComics) }) {
            Text(text = if(showComics.value){"Hide Comics"}else{"Show Comics"})
        }
        GetDescription(textIsShown = showComics, textValue = textComics)


    }
}

@Composable
fun GetDescription(textIsShown: MutableState<Boolean>, textValue: List<String>?) {
    if (textIsShown.value) {
        if (textValue?.isNotEmpty() == true) {
          ShowDetailsText(textValue = textValue)
        } else {
            Text(text = "No stories found")
        }
    }

}

@Composable
fun ShowDetailsText(textValue: List<String>) {
    Box(
        Modifier
            .padding(all = 10.dp)
            .wrapContentHeight()
    ) {
        LazyColumn {
            items(textValue.size) { index ->
                Text(
                    text = textValue[index],
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                if (index < textValue.size - 1) {
                    Divider(color = Color.Gray, thickness = 2.dp)
                }
            }
        }
    }
}