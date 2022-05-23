package com.example.featureGames.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.GameNetworkExceptions
import com.example.core.domain.entities.tools.Message
import com.example.core.domain.entities.tools.NetworkConnectionListener.isNetworkConnected
import com.example.core.domain.entities.tools.NetworkConnectionListener.networkConnectionChanges
import com.example.core.domain.entities.tools.SingleEvent
import com.example.core.domain.entities.tools.constants.Constants
import com.example.core.domain.entities.tools.constants.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.entities.tools.constants.Messages.allGamesHaveBeenLoadedMessage
import com.example.core.domain.entities.tools.constants.StringConstants.PAGE_NOT_FOUND
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.enums.ResponseCodes
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.games.Game
import com.example.core.domain.games.GameBackgroundImageChanges
import com.example.core.domain.games.GameScreenInfo
import com.example.core.domain.games.NewGamesForScreen
import com.example.core.domain.games.interfaces.GameScreenItemType
import com.example.core.domain.games.useCases.LikeGameUseCase
import com.example.core.domain.interfaces.OnNewGetRequestCallback
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.interfaces.Stateful
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.featureGames.domain.entities.GameErrorPagePlaceHolder
import com.example.featureGames.domain.entities.GamePlaceHolder
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCase
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCaseFactory
import com.example.featureGames.presentation.recyclerView.delegates.GameErrorPageAdapterDelegateCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

private typealias NewGamesListEvent = SingleEvent<List<BaseRecyclerViewType>>
private typealias PageToListRecyclerViewItems = MutableMap<Int, MutableList<BaseRecyclerViewType>>

sealed interface GamesVMStats<out T> : Stateful.State {
    val value: SingleEvent<out T>

    class Default(override val value: SingleEvent<out Unit> = SingleEvent(Unit)) :
        GamesVMStats<Unit>

    class AllGamesFromRequestHaveBeenLoaded(
        override val value: SingleEvent<out Message> = SingleEvent(allGamesHaveBeenLoadedMessage)
    ) : GamesVMStats<Message>

    class Error(override val value: SingleEvent<out Message>) : GamesVMStats<Message>,
        Stateful.TerminatingState
}

sealed interface GamesVMSingleEvents<out T> {
    val event: SingleEvent<out T>

    class NewGamesEvent(override val event: NewGamesListEvent) :
        GamesVMSingleEvents<List<BaseRecyclerViewType>>

    class NetworkConnectionLostEvent(override val event: SingleEvent<Unit> = SingleEvent(Unit)) :
        GamesVMSingleEvents<Unit>
}

class GamesViewModel(
    val screenTag: GameScreenTags,
    gamesUseCaseFactory: ReadGamesUseCaseFactory,
    private val likeGameUseCase: LikeGameUseCase,
    private val scopeForAsyncWork: CoroutineScope = CoroutineScope(SupervisorJob() + Main.immediate)
) : ViewModel(), OnPositionChangeListener, Stateful,
    GameScreenInfo.Mapper<PageToListRecyclerViewItems>, GameErrorPageAdapterDelegateCallback,
    OnNewGetRequestCallback<GamesGetRequest> {
    var snackBarIsShowing = false
    private var readGamesUseCase: ReadGamesUseCase =
        gamesUseCaseFactory.create(screenTag, scopeForAsyncWork)
    private var gameScreenInfo = readGamesUseCase.getScreenInfo()
    private var currentScreenItems = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()
    private val _singleEvents = MutableLiveData<GamesVMSingleEvents<Any>>()
    val singleEvents: LiveData<GamesVMSingleEvents<Any>> = _singleEvents
    private val _state = MutableLiveData<GamesVMStats<Any>>()
    val state: LiveData<GamesVMStats<Any>> = _state

    init {
        listenChanges()
        viewModelScope.launch {
            networkConnectionChanges.collect { isConnected ->
                if (isConnected) readGamesUseCase.onNetworkConnected(scopeForAsyncWork)
                else {
                    scopeForAsyncWork.coroutineContext.job.cancelChildren()
                    _singleEvents.value = GamesVMSingleEvents.NetworkConnectionLostEvent()
                }
            }
        }
    }

    fun getGames() {
        if (gameScreenInfo.screenItems.isEmpty()) {
            currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
            scopeForAsyncWork.launch {
                readGamesUseCase.readGames(gameScreenInfo.request.copy())
            }
        } else currentScreenItems = mapGameScreenInfo(gameScreenInfo)
        onNewGameScreenItemsEvent()
    }

    fun onLikeButtonClick(game: Game) {
        if (game.gameEntity.isLiked) likeGameUseCase.unlikeGame(game)
        else likeGameUseCase.likeGame(game)
    }

    override fun onGameErrorPagePlaceHolderClick(page: Int) {
        // To avoid multiple clicks
        if (currentScreenItems[page]?.first() is GameErrorPagePlaceHolder) {
            currentScreenItems[page] = getPlaceholders().toMutableList()
            logD("currentScreenItems: ${currentScreenItems.keys}")
            onNewGameScreenItemsEvent()
            scopeForAsyncWork.launch {
                readGamesUseCase.readGames(gameScreenInfo.request.copy(page = page))
            }
        }
    }

    override fun mapGameScreenInfo(
        gameScreenInfo: GameScreenInfo
    ): PageToListRecyclerViewItems {
        val newMap = mutableMapOf<Int, MutableList<BaseRecyclerViewType>>()
        gameScreenInfo.screenItems.forEach { (key, value) ->
            newMap[key] = when (value) {
                is GameScreenItemType.GameType ->
                    readGamesUseCase.getGamesByPage(value.page)
                is GameScreenItemType.GamePlaceHolderType ->
                    getPlaceholders(value.placeholderCount)
                is GameScreenItemType.GameErrorPageType ->
                    listOf(GameErrorPagePlaceHolder(value.page))
            }.toMutableList()
        }
        return newMap
    }


    override fun onNewPosition(positions: IntArray, itemCount: Int) {
        val isMinVisiblePosition =
            currentScreenItems.values.sumOf { it.size } - positions[0] <= MIN_ITEMS_COUNT_FOR_NEXT_PAGE
        if (state.value is GamesVMStats.AllGamesFromRequestHaveBeenLoaded) return
        else if (isMinVisiblePosition && isNetworkConnected()) loadNextPage()
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as? GamesVMStats<Any> ?: throw TypeCastException()
        if (newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GamesVMStats.Default()
    }

    private fun listenChanges() {
        viewModelScope.launch {
            readGamesUseCase.newGamesForScreen.collect(::onNewGamesForScreen)
        }
        viewModelScope.launch {
            readGamesUseCase.responseHttpExceptions.collect(::onHttpException)
        }
        viewModelScope.launch {
            readGamesUseCase.gamesBackgroundImageChanges.collect(::onNewGamesBackgroundImageChanges)
        }
    }

    private fun loadNextPage() {
        logD("+++++++++loadNextPage+++++++++")
        gameScreenInfo.request = gameScreenInfo.request.next()
        currentScreenItems[gameScreenInfo.request.getPage()] = getPlaceholders().toMutableList()
        onNewGameScreenItemsEvent()
        scopeForAsyncWork.launch {
            //Use copy to create a new request with the same params but different link
            //to don't change the page after adding new placeholders
            readGamesUseCase.readGames(gameScreenInfo.request.copy())
        }
    }

    private fun getPlaceholders(count: Int = Constants.PAGE_SIZE): List<GamePlaceHolder> {
        val result = mutableListOf<GamePlaceHolder>()
        repeat(count) { result.add(GamePlaceHolder()) }
        return result
    }

    private fun prepareListForView(): List<BaseRecyclerViewType> =
        currentScreenItems.keys.sorted().flatMap { currentScreenItems[it]!! }

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
                    else -> addGamePageErrorPlaceHolder(networkException.page)
                }
            }
            is GameNetworkExceptions.GameSocketException -> addGamePageErrorPlaceHolder(
                networkException.page
            )
            is GameNetworkExceptions.DefaultPageException -> addGamePageErrorPlaceHolder(
                networkException.page
            )
        }
    }

    private fun addGamePageErrorPlaceHolder(page: Int) =
        with(page) {
            gameScreenInfo.screenItems[this] = GameScreenItemType.GameErrorPageType(this)
            currentScreenItems[this] = listOf(GameErrorPagePlaceHolder(this)).toMutableList()
            onNewGameScreenItemsEvent()
        }


    private fun removeLastPageOfPlaceHolders(page: Int) {
        if (currentScreenItems.remove(page) == null)
            logE("removeLastPageOfPlaceHolders:$PAGE_NOT_FOUND$page")
//            throw IllegalArgumentException(PAGE_NOT_FOUND + page)
        else onNewGameScreenItemsEvent()
    }

    private fun onNewGamesForScreen(changes: NewGamesForScreen) {
        if (this.gameScreenInfo.tag != changes.screenTag) return
        logE("collect: onNewGamesForScreen, tag: ${gameScreenInfo.tag}")
        currentScreenItems[changes.page] =
            readGamesUseCase.getGamesByPage(changes.page).toMutableList()
        onNewGameScreenItemsEvent()
    }

    private fun onNewGamesBackgroundImageChanges(changes: GameBackgroundImageChanges) {
        if (this.gameScreenInfo.tag != changes.screenTag || currentScreenItems.isEmpty()) return
        currentScreenItems[changes.page]!!.indexOfFirst { type ->
            if (type is Game) type.gameEntity.id == changes.game.gameEntity.id else false
        }.also { index ->
            if (index == -1) {
                // onNewRequest but pictures come from an ald request
                logE("onNewGamesBackgroundImageChanges: game not fond: ${changes.game.gameEntity.name}")
                return
            }
            currentScreenItems[changes.page]!![index] = changes.game
            onNewGameScreenItemsEvent()
        }
    }

    override fun onNewRequest(request: GamesGetRequest) {
        scopeForAsyncWork.coroutineContext.job.cancelChildren()
        currentScreenItems.clear()
        onNewGameScreenItemsEvent()
        gameScreenInfo.screenItems.clear()
        gameScreenInfo.request = request
        gameScreenInfo.screenItems[request.getPage()] =
            GameScreenItemType.GamePlaceHolderType(request.getPage())
        currentScreenItems[request.getPage()] = getPlaceholders().toMutableList()
        scopeForAsyncWork.launch {
            readGamesUseCase.readGames(gameScreenInfo.request.copy())
        }
        resetState()
        onNewGameScreenItemsEvent()
    }

}

