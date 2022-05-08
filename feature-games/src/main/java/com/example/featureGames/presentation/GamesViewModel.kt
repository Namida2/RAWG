package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.GameNetworkExceptions
import com.example.core.domain.entities.Message
import com.example.core.domain.entities.NetworkConnectionListener.isNetworkConnected
import com.example.core.domain.entities.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.entities.SingleEvent
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.tools.constants.Constants
import com.example.core.domain.tools.constants.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.constants.Messages.allGamesHaveBeenLoadedMessage
import com.example.core.domain.tools.constants.Messages.badGatewayMessage
import com.example.core.domain.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.tools.constants.StringConstants.GAME_NOT_FOUND
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.enums.ResponseCodes
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import com.example.featureGames.domain.useCase.GamesUseCase
import com.example.featureGames.domain.useCase.GamesUseCaseFactory
import com.example.featureGames.presentation.recyclerView.delegates.GameErrorPageAdapterDelegateCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

private typealias NewGamesListEvent = SingleEvent<List<BaseRecyclerViewType>>
private typealias PageToListRecyclerViewItems = MutableMap<Int, MutableList<BaseRecyclerViewType>>

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
    screenTag: GameScreenTags,
    gamesUseCaseFactory: GamesUseCaseFactory,
    private val remoteRepositoryScope: CoroutineScope = CoroutineScope(SupervisorJob() + Main.immediate)
) : ViewModel(), OnPositionChangeListener, Stateful,
    GameScreenInfo.Mapper<PageToListRecyclerViewItems>, GameErrorPageAdapterDelegateCallback {
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
                logD(screenTag.toString())
                if (isConnected) gamesUseCase.onNetworkConnected(remoteRepositoryScope)
                else {
                    remoteRepositoryScope.coroutineContext.job.cancelChildren()
                    _singleEvents.value = GamesVMSingleEvents.NetworkConnectionLostEvent()
                }
            }
        }
    }

    fun getGames() {
        logD("getGames: ${gameScreenInfo.tag}")
        if (gameScreenInfo.screenItems.isEmpty()) {
            viewModelScope.launch {
                gamesUseCase.readGames(gameScreenInfo.request.copy())
            }
            currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        } else currentScreenItems = mapGameScreenInfo(gameScreenInfo)
        onNewGameScreenItemsEvent()
    }

    override fun onGameErrorPagePlaceHolderClick(page: Int) {
        TODO("Not yet implemented")
    }

    override fun mapGameScreenInfo(
        gameScreenInfo: GameScreenInfo
    ): PageToListRecyclerViewItems {
        val newMap = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()
        gameScreenInfo.screenItems.forEach { (key, value) ->
            newMap[key] = when (value) {
                is GameScreenItemType.GameType ->
                    gamesUseCase.getGamesByPage(value.page)
                is GameScreenItemType.GamePlaceHolderType ->
                    getPlaceholders(value.placeholderCount)
                is GameScreenItemType.GameErrorPageType ->
                    listOf(GameErrorPagePlaceHolder(value.page))
            }.toMutableList()
        }
        return newMap
    }


    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        logD("onNewPosition: ${positions.toList()}, size: ${currentScreenItems.values.sumOf { it.size }}")
        val isMinVisiblePosition =
            currentScreenItems.values.sumOf { it.size } - positions[0] <= MIN_ITEMS_COUNT_FOR_NEXT_PAGE
        if (state.value is GamesVMStats.AllGamesFromRequestHaveBeenLoaded && isMinVisiblePosition) return
        else if (isMinVisiblePosition && isNetworkConnected())
            loadNextPage()
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as? GamesVMStats ?: throw TypeCastException()
        if (newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GamesVMStats.Default
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
            gamesUseCase.readGames(gameScreenInfo.request.copy())
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

    private fun onNewGameScreenItemsEvent() {
        _singleEvents.value = GamesVMSingleEvents.NewGamesEvent(
            SingleEvent(prepareListForView())
        )
    }

    private fun onHttpException(networkException: GameNetworkExceptions) {
        when (networkException) {
            is GameNetworkExceptions.GamesHttpException -> {
                logD("onHttpException: ${networkException.exception.code()} ${networkException.exception.message()}")
                when (networkException.exception.code()) {
                    ResponseCodes.PAGE_NOT_FOUND.code -> {
                        setNewState(GamesVMStats.AllGamesFromRequestHaveBeenLoaded())
                        removeLastPageOfPlaceHolders(networkException.page)
                    }
                    ResponseCodes.BAD_GATEWAY.code -> setNewState(
                        GamesVMStats.Error(
                            badGatewayMessage
                        )
                    )
                    else -> setNewState(GamesVMStats.Error())
                }
            }
            is GameNetworkExceptions.GameSocketException ->
                with(networkException.page) {
                    // TODO: Update the gameScreenInfo for saving current
                    //  gameScreenItem when current viewModel is destroyed
                    gameScreenInfo.screenItems[this] = GameScreenItemType.GameErrorPageType(this)
                    currentScreenItems[this] = listOf(GameErrorPagePlaceHolder(this)).toMutableList()
                    onNewGameScreenItemsEvent()
                }
        }
    }

    private fun removeLastPageOfPlaceHolders(page: Int) {
//        logE("IllegalArgumentException, page+++: $page")
        if (currentScreenItems.remove(page) == null) {
            logE("IllegalArgumentException, page: $page")
            throw IllegalArgumentException()
        } else onNewGameScreenItemsEvent()

    }

    private fun onNewGamesForScreen(changes: NewGamesForScreen) {
//        logD("GamesViewModel.init. page: ${changes.page}")
        if (this.gameScreenInfo.tag != changes.screenTag) return
        currentScreenItems[changes.page] =
            gamesUseCase.getGamesByPage(changes.page).toMutableList()
        onNewGameScreenItemsEvent()
    }

    private fun onNewGamesBackgroundImageChanges(changes: GameBackgroundImageChanges) {
//        logD("gamesBackgroundImageChanges, page: ${changes.page}")
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

