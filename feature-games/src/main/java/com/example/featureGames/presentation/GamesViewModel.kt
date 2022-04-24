package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

typealias NewGamesListEvent = Event<List<Game>>

@OptIn(FlowPreview::class)
class GamesViewModel(
    screenTag: GameScreens,
    private val gamesUseCase: GamesUseCase,
) : ViewModel(), OnPositionChangeListener {

    private var gameScreenInfo = gamesUseCase.getScreenInfo(screenTag)
    private var currentScreenGames = listOf<Game>()
    private val _newGamesEvent = MutableLiveData<NewGamesListEvent>()
    val newGamesEvent: LiveData<NewGamesListEvent> = _newGamesEvent

    init {
        viewModelScope.launch {
            gamesUseCase.gameScreenChanges.debounce(200).collect { screenTag ->
                if (this@GamesViewModel.gameScreenInfo.tag == screenTag) {
                    currentScreenGames = gamesUseCase.getGamesBuScreenTag(screenTag)
                    _newGamesEvent.value = Event(currentScreenGames)
                }
            }
        }
    }

    fun readGames() {
        viewModelScope.launch {
            currentScreenGames = gamesUseCase.readGames(
                gameScreenInfo.tag,
                gameScreenInfo.request
            )
            _newGamesEvent.value = Event(currentScreenGames)
        }
    }

    // TODO: Add a request queue //STOPPED//
    override fun onNewPosition(position: Int, itemCount: Int) {
        logD("onNewPosition: $position, size: $itemCount")
    }
}

