package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Event
import com.example.core.domain.tools.NetworkConnectionListener.isNetworkConnected
import com.example.core.domain.tools.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.tools.constants.Constants
import com.example.core.domain.tools.constants.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.constants.StringConstants.GAME_NOT_FOUND
import com.example.core.domain.tools.extensions.ResponseCodes
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.interfaces.Stateful
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GameBackgroundImageChanges
import com.example.featureGames.domain.model.GamePlaceHolder
import com.example.featureGames.domain.model.NewGamesForScreen
import com.example.featureGames.domain.useCase.GamesUseCase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException

typealias NewGamesListEvent = Event<List<BaseRecyclerViewType>>

sealed class GamesVMStats : Stateful.State {
    object Default : GamesVMStats()
    object AllGamesFromRequestHaveBeenLoaded: GamesVMStats()
}

class GamesViewModel(
    private val gamesUseCase: GamesUseCase,
) : ViewModel(), OnPositionChangeListener, Stateful {
    private var gameScreenInfo = gamesUseCase.getScreenInfo()
    private var currentScreenItems = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()
    private val _newGamesEvent = MutableLiveData<NewGamesListEvent>()
    val newGamesEvent: LiveData<NewGamesListEvent> = _newGamesEvent

    private val _networkConnectionLostEvent = MutableLiveData<Event<Unit>>()
    val networkConnectionLostEvent: LiveData<Event<Unit>> = _networkConnectionLostEvent

    private val _state = MutableLiveData<GamesVMStats>()
    val state: LiveData<GamesVMStats> = _state

    private val remoteRepositoryScope = CoroutineScope(Main.immediate)
    init {
        listenChanges()
        viewModelScope.launch {
            networkConnectionChanges.collect { isConnected ->
                if (isConnected) {
                    listenChanges()
                    gamesUseCase.onNetworkConnected(this)
                }
                else {
                    remoteRepositoryScope.coroutineContext.cancelChildren()
                    _networkConnectionLostEvent.value = Event(Unit)
                }
            }
        }
    }

    private fun listenChanges() {
        remoteRepositoryScope.launch {
            gamesUseCase.newGamesForScreen.collect(::onNewGamesForScreen)
        }
        remoteRepositoryScope.launch {
            gamesUseCase.responseHttpExceptions.collect(::onHttpException)
        }
        remoteRepositoryScope.launch {
            gamesUseCase.gamesBackgroundImageChanges.collect(::onNewGamesBackgroundImageChanges)
        }
    }

    fun readGames() {
        viewModelScope.launch {
            gamesUseCase.readGames(gameScreenInfo.request.copy(), viewModelScope)
        }
        currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        _newGamesEvent.value = Event(prepareListForView())
    }

    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        logD("onNewPosition: ${positions.toList()}, size: ${currentScreenItems.values.sumOf { it.size }}")
        if (currentScreenItems.values.sumOf { it.size } - positions[0] <= MIN_ITEMS_COUNT_FOR_NEXT_PAGE && isNetworkConnected()
            && state.value != GamesVMStats.AllGamesFromRequestHaveBeenLoaded)
            loadNextPage()
    }

    private fun loadNextPage() {
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

    private fun getPlaceholders(count: Int = Constants.PAGE_SIZE): List<GamePlaceHolder> {
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

    private fun onHttpException(exception: HttpException) {
        when (exception.code()) {
            ResponseCodes.PAGE_NOT_FOUND.code -> {}
            ResponseCodes.BAD_GATEWAY.code -> {}
            else -> {}
        }
    }

    private fun onNewGamesForScreen(changes: NewGamesForScreen) {
        logD("GamesViewModel.init. page: ${changes.page}")
        if (this.gameScreenInfo.tag != changes.screenTag) return
        currentScreenItems[changes.page] =
            gamesUseCase.getGamesByPage(changes.page).toMutableList()
        _newGamesEvent.value = Event(prepareListForView())
    }

    private fun onNewGamesBackgroundImageChanges(changes: GameBackgroundImageChanges) {
        logD("gamesBackgroundImageChanges, page: $changes")
        if (this.gameScreenInfo.tag != changes.screenTag) return
        currentScreenItems[changes.page]!!.indexOfFirst { type ->
            if (type !is Game) false
            else type.id == changes.game.id
        }.also { index ->
            if (index == -1) throw IllegalArgumentException(GAME_NOT_FOUND + changes.game.id)
            currentScreenItems[changes.page]!![index] = changes.game
            _newGamesEvent.value = Event(prepareListForView())
        }
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as? GamesVMStats ?: throw TypeCastException()
        if (newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GamesVMStats.Default
    }

    override fun onCleared() {
        remoteRepositoryScope.cancel()
        super.onCleared()
    }
}

