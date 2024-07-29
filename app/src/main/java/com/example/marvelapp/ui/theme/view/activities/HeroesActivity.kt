package com.example.marvelapp.ui.theme.view.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.marvelapp.ui.theme.view.pages.GetContent
import com.example.marvelapp.ui.theme.viewModel.HeroesViewModel

class HeroesActivity : AppCompatActivity() {
    private lateinit var viewModel: HeroesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = HeroesViewModel()
        val stories = intent.getStringArrayListExtra("stories")
        val events = intent.getStringArrayListExtra("events")
        val comics = intent.getStringArrayListExtra("comics")
        setContent {
            if (stories != null && events != null && comics != null)
                GetContent(
                    stories = stories,
                    events = events,
                    comics = comics,
                    { viewModel.toggleStoriesVisibility(it) },
                    { viewModel.toggleEventsVisibility(it) },
                    { viewModel.toggleComicsVisibility(it) }
                )
        }
    }
}
