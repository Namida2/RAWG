package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Event
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.launch

typealias NewGamesListEvent = Event<List<Game>>

class GamesViewModel(
    private val gamesUseCase: GamesUseCase
): ViewModel() {

    private val _newGamesEvent = MutableLiveData<NewGamesListEvent>()
    val newGamesEvent: LiveData<NewGamesListEvent> = _newGamesEvent

    fun readGames() {
        viewModelScope.launch {
            val games = gamesUseCase.getGames()
            _newGamesEvent.value = Event(games)
        }
    }
}