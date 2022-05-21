package com.example.featureGameDetails.domain.entities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.entities.tools.constants.StringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureGameDetails.domain.di.GameDetailsDepsStore
import com.example.featureGameDetails.presentation.GameDetailsViewModel

class ViewModelFactory(private val gameId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when (modelClass) {
            GameDetailsViewModel::class.java ->
                GameDetailsViewModel(
                    gameId,
                    GameDetailsDepsStore.appComponent!!.provideGetGameDetailsUseCaseImplFactory()
                )
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS)
        } as T
}