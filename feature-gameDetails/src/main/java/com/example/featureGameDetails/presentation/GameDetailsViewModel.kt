package com.example.featureGameDetails.presentation

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.tools.Message
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.entities.tools.SingleEvent
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.tools.extensions.logD
import com.example.core_game.domain.Game
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCaseFactory
import kotlinx.coroutines.*

sealed interface GameDetailsVMEvents<T> {
    val value: SingleEvent<out T>

    class NewScreenshotsListEvent(
        override val value: SingleEvent<List<GameScreenshot>>
    ) : GameDetailsVMEvents<List<GameScreenshot>>

    object LostNetworkConnectionEvent : GameDetailsVMEvents<Unit> {
        override val value: SingleEvent<out Unit> = SingleEvent(Unit)
    }

    class OnError(
        override val value: SingleEvent<out Message>
    ) : GameDetailsVMEvents<Message>
}

sealed class GameDetailsVMEStates : Stateful.State {
    class GameDetailsExists(val game: Game) : GameDetailsVMEStates()
    object ReadingGameDetails : GameDetailsVMEStates()
    object Default : GameDetailsVMEStates()
}

class GameDetailsViewModel(
    gameId: Int,
    getGameDetailsUseCaseFactory: GetGameDetailsUseCaseFactory,
    private val scopeForAsyncWork: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : ViewModel(), Stateful {
    private var currentGame: Game? = null
    private val currentGameScreenshots = mutableListOf<GameScreenshot>()
    private val getGameDetailsUseCase = getGameDetailsUseCaseFactory.create(scopeForAsyncWork)
    private val _events = MutableLiveData<GameDetailsVMEvents<out Any>>()
    private val _state = MutableLiveData<GameDetailsVMEStates>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        resetState()
        logD("$coroutineContext: $throwable")
        _events.value = GameDetailsVMEvents.OnError(SingleEvent(defaultErrorMessage))
    }
    val events: LiveData<GameDetailsVMEvents<out Any>> = _events
    val state: LiveData<GameDetailsVMEStates> = _state

    init {
        viewModelScope.launch {
            getGameDetailsUseCase.onNewScreenshotLoaded.collect {
                addScreenshots(listOf(it))
                _events.value = GameDetailsVMEvents.NewScreenshotsListEvent(
                    SingleEvent(currentGameScreenshots.toMutableList())
                )
            }
        }
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect { isConnected ->
                if (isConnected)
                    if (currentGame == null) getDetails(gameId)
                    else getGameDetailsUseCase.onNetworkConnected(gameId)
                else {
                    scopeForAsyncWork.coroutineContext.job.cancelChildren()
                    _events.value = GameDetailsVMEvents.LostNetworkConnectionEvent
                }
            }
        }
    }

    fun getDetails(gameId: Int) {
        if (state.value == GameDetailsVMEStates.ReadingGameDetails) return
        setNewState(GameDetailsVMEStates.ReadingGameDetails)
        scopeForAsyncWork.launch(exceptionHandler) {
            currentGame = getGameDetailsUseCase.getGameDetails(gameId)
            setNewState(GameDetailsVMEStates.GameDetailsExists(currentGame!!))
            addScreenshots(currentGame?.shortScreenshots?.values?.toList() ?: return@launch)
        }
    }

    private suspend fun addScreenshots(list: List<Bitmap?>) {
        withContext(viewModelScope.coroutineContext) {
            list.forEach { bitmap ->
                val gameScreenshot = GameScreenshot(bitmap ?: return@forEach)
                if (currentGameScreenshots.contains(gameScreenshot)) return@forEach
                currentGameScreenshots.add(gameScreenshot)
            }
            if (currentGameScreenshots.size == 0) return@withContext
            _events.value = GameDetailsVMEvents.NewScreenshotsListEvent(
                SingleEvent(currentGameScreenshots.toMutableList())
            )
        }
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as GameDetailsVMEStates
        if (newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GameDetailsVMEStates.Default
    }

}