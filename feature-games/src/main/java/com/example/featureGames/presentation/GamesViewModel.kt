package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Constants
import com.example.core.domain.tools.Constants.GAMES_SPAN_COUNT
import com.example.core.domain.tools.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

typealias NewGamesListEvent = Event<List<Game>>

sealed class GamesVMStates {
    object Default : GamesVMStates()
    object LoadingNextPage : GamesVMStates()
}

@OptIn(FlowPreview::class)
class GamesViewModel(
    screenTag: GameScreens,
    private val gamesUseCase: GamesUseCase,
) : ViewModel(), OnPositionChangeListener {
    private var gameScreenInfo = gamesUseCase.getScreenInfo(screenTag)
    private var currentScreenGames = listOf<Game>()
    private val _newGamesEvent = MutableLiveData<NewGamesListEvent>()

    val newGamesEvent: LiveData<NewGamesListEvent> = _newGamesEvent
    private val _loadingNewPageEvent = MutableLiveData<Event<IntArray>>()
    val loadingNewPageEvent: LiveData<Event<IntArray>> = _loadingNewPageEvent

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
            gamesUseCase.readGames(gameScreenInfo.tag, gameScreenInfo.request)
        }
    }

    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        if (itemCount - positions[0] < MIN_ITEMS_COUNT_FOR_NEXT_PAGE)
            _loadingNewPageEvent.value = Event(positions)
        logD("onNewPosition: ${positions.toList()}, size: $itemCount")
    }

    fun loadNextPage() {
        logD("loadNextPage")
        viewModelScope.launch {
            gamesUseCase.readGames(
                gameScreenInfo.tag,
                gameScreenInfo.request.next())
        }
    }
}

