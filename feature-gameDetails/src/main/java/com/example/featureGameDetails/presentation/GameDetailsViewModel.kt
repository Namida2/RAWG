package com.example.featureGameDetails.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_game.domain.Game
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCase
import kotlinx.coroutines.launch

class GameDetailsViewModel(
    private val getGameDetailsUseCase: GetGameDetailsUseCase
) : ViewModel() {


    fun getDetails(gameId: Int) {
        viewModelScope.launch {
            getGameDetailsUseCase.getGameDetails(gameId)
        }
    }

    fun getGameById(gameId: Int): Game =
        getGameDetailsUseCase.getGameById(gameId)


}