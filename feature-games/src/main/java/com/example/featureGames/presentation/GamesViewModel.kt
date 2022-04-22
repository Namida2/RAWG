package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.Event
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Connection
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

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

