package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.GamesHttpException
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Message
import com.example.core.domain.tools.NetworkConnectionListener.isNetworkConnected
import com.example.core.domain.tools.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.tools.SingleEvent
import com.example.core.domain.tools.constants.Constants
import com.example.core.domain.tools.constants.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.constants.Messages.allGamesHaveBeenLoadedMessage
import com.example.core.domain.tools.constants.Messages.badGatewayMessage
import com.example.core.domain.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.tools.constants.StringConstants.GAME_NOT_FOUND
import com.example.core.domain.tools.enums.GameScreens
import com.example.core.domain.tools.enums.ResponseCodes
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.interfaces.Stateful
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GameBackgroundImageChanges
import com.example.featureGames.domain.model.GamePlaceHolder
import com.example.featureGames.domain.model.NewGamesForScreen
import com.example.featureGames.domain.useCase.GamesUseCase
import com.example.featureGames.domain.useCase.GamesUseCaseFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException

typealias NewGamesListEvent = SingleEvent<List<BaseRecyclerViewType>>
sealed class GamesVMStats : Stateful.State {
    object Default : GamesVMStats()
    class AllGamesFromRequestHaveBeenLoaded(
        val message: Message = allGamesHaveBeenLoadedMessage
    ) : GamesVMStats()

    class Error(
        val message: Message = defaultErrorMessage
    ) : GamesVMStats(), Stateful.TerminatingState
}
sealed interface GamesVMSingleEvents<out T> {
    val event: SingleEvent<out T>

    class NewGamesEvent(override val event: NewGamesListEvent) :
        GamesVMSingleEvents<List<BaseRecyclerViewType>>

    class NetworkConnectionLostEvent(override val event: SingleEvent<Unit> = SingleEvent(Unit)) :
        GamesVMSingleEvents<Unit>
}

class GamesViewModel(
    screenTag: GameScreens,
    gamesUseCaseFactory: GamesUseCaseFactory,
    private val remoteRepositoryScope: CoroutineScope = CoroutineScope(SupervisorJob() + Main.immediate)
) : ViewModel(), OnPositionChangeListener, Stateful {
    var snackBarIsShowing = false
    private var gamesUseCase: GamesUseCase =
        gamesUseCaseFactory.create(screenTag, remoteRepositoryScope)
    private var gameScreenInfo = gamesUseCase.getScreenInfo()
    private var currentScreenItems = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()

    private val _singleEvents = MutableLiveData<GamesVMSingleEvents<Any>>()
    val singleEvents: LiveData<GamesVMSingleEvents<Any>> = _singleEvents

    private val _state = MutableLiveData<GamesVMStats>()
    val state: LiveData<GamesVMStats> = _state

    init {
        listenChanges()
        viewModelScope.launch {
            networkConnectionChanges.collect { isConnected ->
                if (isConnected) gamesUseCase.onNetworkConnected(remoteRepositoryScope)
                else {
                    remoteRepositoryScope.coroutineContext.job.cancelChildren()
                    _singleEvents.value = GamesVMSingleEvents.NetworkConnectionLostEvent()
                }
            }
        }
    }

    fun readGames() {
        viewModelScope.launch {
            gamesUseCase.readGames(gameScreenInfo.request.copy(), remoteRepositoryScope)
        }
        currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        onNewGameScreenItemsEvent()
    }

    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        logD("onNewPosition: ${positions.toList()}, size: ${currentScreenItems.values.sumOf { it.size }}")
        val isMinVisiblePosition = currentScreenItems.values.sumOf { it.size } - positions[0] <= MIN_ITEMS_COUNT_FOR_NEXT_PAGE
        if(state.value is GamesVMStats.AllGamesFromRequestHaveBeenLoaded && isMinVisiblePosition ) return
        else if (isMinVisiblePosition && isNetworkConnected())
            loadNextPage()
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as? GamesVMStats ?: throw TypeCastException()
        if(newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GamesVMStats.Default
    }

    override fun onCleared() {
        remoteRepositoryScope.cancel()
        super.onCleared()
    }

    private fun listenChanges() {
        viewModelScope.launch {
            gamesUseCase.newGamesForScreen.collect(::onNewGamesForScreen)
        }
        viewModelScope.launch {
            gamesUseCase.responseHttpExceptions.collect(::onHttpException)
        }
        viewModelScope.launch {
            gamesUseCase.gamesBackgroundImageChanges.collect(::onNewGamesBackgroundImageChanges)
        }
    }

    private fun loadNextPage() {
        logD("+++++++++loadNextPage+++++++++")
        gameScreenInfo.request = gameScreenInfo.request.next()
        currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        onNewGameScreenItemsEvent()
        viewModelScope.launch {
            //Use copy to create a new request with the same params but different link
            //to don't change the page after adding new placeholders
            gamesUseCase.readGames(gameScreenInfo.request.copy(), remoteRepositoryScope)
        }
    }

    private fun getPlaceholders(count: Int = Constants.PAGE_SIZE): List<GamePlaceHolder> {
        val placeHolder = GamePlaceHolder()
        val result = mutableListOf<GamePlaceHolder>()
        return repeat(count) {
            result.add(placeHolder)
        }.run { result }
    }

    private fun removeLastPageOfPlaceHolders(page: Int) {
        if(currentScreenItems.remove(page) == null)
            throw IllegalArgumentException()
        else onNewGameScreenItemsEvent()

    }

    private fun prepareListForView(): List<BaseRecyclerViewType> =
        currentScreenItems.keys.sorted().map {
            currentScreenItems[it]!!
        }.flatten()

    private fun onNewGameScreenItemsEvent() {
        _singleEvents.value = GamesVMSingleEvents.NewGamesEvent(SingleEvent(prepareListForView()))
    }

    private fun onHttpException(exception: GamesHttpException) {
        logD("onHttpException: ${exception.exception.code()} ${exception.exception.message()}")
        when (exception.exception.code()) {
            ResponseCodes.PAGE_NOT_FOUND.code -> {
                setNewState(GamesVMStats.AllGamesFromRequestHaveBeenLoaded())
                removeLastPageOfPlaceHolders(exception.page)
            }
            ResponseCodes.BAD_GATEWAY.code -> setNewState(GamesVMStats.Error(badGatewayMessage))
            else -> setNewState(GamesVMStats.Error())
        }
    }

    private fun onNewGamesForScreen(changes: NewGamesForScreen) {
        logD("GamesViewModel.init. page: ${changes.page}")
        if (this.gameScreenInfo.tag != changes.screenTag) return
        currentScreenItems[changes.page] =
            gamesUseCase.getGamesByPage(changes.page).toMutableList()
        onNewGameScreenItemsEvent()
    }

    private fun onNewGamesBackgroundImageChanges(changes: GameBackgroundImageChanges) {
        logD("gamesBackgroundImageChanges, page: $changes")
        if (this.gameScreenInfo.tag != changes.screenTag) return
        currentScreenItems[changes.page]!!.indexOfFirst { type ->
            if (type is Game) type.id == changes.game.id else false
        }.also { index ->
            if (index == -1) throw IllegalArgumentException(GAME_NOT_FOUND + changes.game.id)
            currentScreenItems[changes.page]!![index] = changes.game
            onNewGameScreenItemsEvent()
        }
    }
}

