package com.example.featureGameDetails.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.entities.tools.SingleEvent
import com.example.core.domain.interfaces.Stateful
import com.example.core_game.domain.Game
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCaseFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

sealed interface GameDetailsVMEvents<T> {
    val value: SingleEvent<out T>
    class NewScreenshotsListEvent(
        override val value: SingleEvent<List<GameScreenshot>>
    ) : GameDetailsVMEvents<List<GameScreenshot>>
    object LostNetworkConnectionEvent : GameDetailsVMEvents<Unit> {
        override val value: SingleEvent<out Unit> = SingleEvent(Unit)
    }
}

sealed class GameDetailsVMEStates: Stateful.State {
    class GameDetailsExists(val game: Game): GameDetailsVMEStates()
    object Default: GameDetailsVMEStates()
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
    val events: LiveData<GameDetailsVMEvents<out Any>> = _events
    val state: LiveData<GameDetailsVMEStates> = _state

    init {
        viewModelScope.launch {
            getGameDetailsUseCase.onNewScreenshotLoaded.collect {
                currentGameScreenshots.add(GameScreenshot(it))
            }
        }
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect { isConnected ->
                if (isConnected)
                    if (currentGame == null) getDetails(gameId)
                    else getGameDetailsUseCase.onNetworkConnected(gameId)
                else _events.value = GameDetailsVMEvents.LostNetworkConnectionEvent
            }
        }
    }

    fun getDetails(gameId: Int) {
        scopeForAsyncWork.launch {
            currentGame = getGameDetailsUseCase.getGameDetails(gameId)
            setNewState(GameDetailsVMEStates.GameDetailsExists(currentGame!!))
        }
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as GameDetailsVMEStates
        if(newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = GameDetailsVMEStates.Default
    }

}