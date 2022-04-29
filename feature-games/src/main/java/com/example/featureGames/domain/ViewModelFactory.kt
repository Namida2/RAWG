package com.example.featureGames.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureGames.domain.di.GamesDepsStore.gamesAppComponent
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.featureGames.presentation.GamesViewModel

class ViewModelFactory(private val screenTag: GameScreenTags) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            GamesViewModel::class.java -> {
                GamesViewModel(
                    screenTag,
                    gamesAppComponent!!.provideAllGamesFactory()
                )
            }
            else -> throw IllegalArgumentException()
        }
        return viewModel as T
    }
}