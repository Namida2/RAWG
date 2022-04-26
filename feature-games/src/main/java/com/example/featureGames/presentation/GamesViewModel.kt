package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Constants
import com.example.core.domain.tools.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.Messages.GAME_NOT_FOUND
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GamePlaceHolder
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

typealias NewGamesListEvent = Event<List<BaseRecyclerViewType>>

@OptIn(FlowPreview::class)
class GamesViewModel(
    private val gamesUseCase: GamesUseCase,
) : ViewModel(), OnPositionChangeListener {
    private var gameScreenInfo = gamesUseCase.getScreenInfo()
    private var currentScreenItems = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()

    private val _newGamesEvent = MutableLiveData<NewGamesListEvent>()
    val newGamesEvent: LiveData<NewGamesListEvent> = _newGamesEvent
    private val _loadingNewPageEvent = MutableLiveData<Event<IntArray>>()
    val loadingNewPageEvent: LiveData<Event<IntArray>> = _loadingNewPageEvent

    init {
        viewModelScope.launch {
            gamesUseCase.newGamesForScreen.debounce(0).collect { changes ->
                logD("GamesViewModel.init. page: ${changes.page}")
                if (this@GamesViewModel.gameScreenInfo.tag != changes.screenTag) return@collect
                currentScreenItems[changes.page] =
                    gamesUseCase.getGamesByPage(changes.page).toMutableList()
                _newGamesEvent.value = Event(prepareListForView())
            }
        }
        viewModelScope.launch {
            gamesUseCase.responseHttpExceptions.collect {
                logD("$this@GamesViewModel: $it")
            }
        }
        viewModelScope.launch {
            gamesUseCase.gamesBackgroundImageChanges.collect { changes ->
                logD("gamesBackgroundImageChanges, page: $changes")
                if (this@GamesViewModel.gameScreenInfo.tag != changes.screenTag) return@collect
                currentScreenItems[changes.page]!!.indexOfFirst { type ->
                    if (type !is Game) false
                    else type.id == changes.game.id
                }.also { index ->
                    if (index == -1) throw IllegalArgumentException(GAME_NOT_FOUND + changes.game.id)
                    currentScreenItems[changes.page]!![index] = changes.game
                    _newGamesEvent.value = Event(prepareListForView())
                }
            }
        }
    }

    fun readGames() {
        viewModelScope.launch {
            gamesUseCase.readGames(gameScreenInfo.request, viewModelScope)
        }
    }

    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        if (itemCount - positions[0] < MIN_ITEMS_COUNT_FOR_NEXT_PAGE)
            _loadingNewPageEvent.value = Event(positions)
        logD("onNewPosition: ${positions.toList()}, size: $itemCount")
    }

    fun loadNextPage() {
        logD("+++++++++loadNextPage+++++++++")
        gameScreenInfo.request = gameScreenInfo.request.next()
        currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        _newGamesEvent.value = Event(prepareListForView())
        viewModelScope.launch {
            //Use copy to create a new request with the same params but different link
            //to don't change the page after adding new placeholders
            gamesUseCase.readGames(gameScreenInfo.request.copy(), viewModelScope)

        }
    }

    fun getPlaceholders(count: Int = Constants.PAGE_SIZE): List<GamePlaceHolder> {
        val placeHolder = GamePlaceHolder()
        val result = mutableListOf<GamePlaceHolder>()
        return repeat(count) {
            result.add(placeHolder)
        }.run { result }
    }

    private fun prepareListForView(): List<BaseRecyclerViewType> =
        currentScreenItems.keys.sorted().map {
            currentScreenItems[it]!!
        }.flatten()

}

