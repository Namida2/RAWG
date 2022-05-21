package com.example.featureGames.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.StringConstants
import com.example.featureGames.domain.di.GamesDepsStore.gamesAppComponent
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.featureGames.presentation.GamesViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val screenTag: GameScreenTags) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            GamesViewModel::class.java -> {
                when(screenTag) {
                    GameScreenTags.MY_LIKES -> GamesViewModel(
                        screenTag,
                        gamesAppComponent!!.provideMyLikesGamesUseCaseFactory(),
                        gamesAppComponent!!.provideLikeGameUseCase()
                    )
                    else -> GamesViewModel(
                        screenTag,
                        gamesAppComponent!!.provideDefaultGamesUseCaseFactory(),
                        gamesAppComponent!!.provideLikeGameUseCase()
                    )
                }
            }
            else -> throw IllegalArgumentException(StringConstants.UNKNOWN_VIEW_MODEL_CLASS + modelClass)
        }
        return viewModel as T
    }
}