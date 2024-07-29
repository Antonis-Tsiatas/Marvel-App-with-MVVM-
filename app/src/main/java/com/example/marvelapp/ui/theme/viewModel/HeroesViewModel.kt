package com.example.marvelapp.ui.theme.viewModel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel

class HeroesViewModel : ViewModel() {
    fun toggleStoriesVisibility(showStories: MutableState<Boolean>) {
        showStories.value = !showStories.value
    }

    fun toggleEventsVisibility(showEvents: MutableState<Boolean>) {
        showEvents.value = !showEvents.value
    }

    fun toggleComicsVisibility(showComics: MutableState<Boolean>) {
        showComics.value = !showComics.value
    }


}
