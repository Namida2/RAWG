package com.example.featureGames.presentation

import androidx.lifecycle.ViewModel
import com.example.featureGames.domain.useCase.GameUseCase

class GamesViewModel(
    private val gameUseCase: GameUseCase
): ViewModel() {

}