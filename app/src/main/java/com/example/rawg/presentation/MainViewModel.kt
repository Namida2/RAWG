package com.example.rawg.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.tools.Message
import com.example.core.domain.entities.tools.NetworkConnectionListener
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.entities.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.extensions.logE
import com.example.rawg.domain.useCases.ReadFiltersUseCase
import com.example.rawg.domain.useCases.ReadGamesFromLocalStorageUseCaseFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import javax.inject.Inject

sealed class MainVMStates : Stateful.State {
    object Default : MainVMStates()
    object ReadingFilters : MainVMStates()
    object LostNetworkConnection : MainVMStates()
    class Error(
        val message: Message = defaultErrorMessage
    ) : MainVMStates(), Stateful.TerminatingState

    object FiltersLoadedSuccessfully : MainVMStates()
}

class MainViewModel @Inject constructor(
    private val readFiltersUseCase: ReadFiltersUseCase,
    readGamesFromLocalStorageUseCaseFactory: ReadGamesFromLocalStorageUseCaseFactory
) : ViewModel(), Stateful {
    private val readGamesUseCase = readGamesFromLocalStorageUseCaseFactory.create(GameScreenTags.MY_LIKES)
    private val _state = MutableLiveData<MainVMStates>(MainVMStates.Default)
    val state: LiveData<MainVMStates> = _state
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logE("$this: throwable: $throwable, coroutineContext: $coroutineContext")
            MainScope().launch { setNewState(MainVMStates.Error()) }
        }

    init {
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect { isConnected ->
                if (isConnected && state.value is MainVMStates.LostNetworkConnection)
                    readFiltersAndMyLikes()
                else if (!isConnected)
                    setNewState(MainVMStates.LostNetworkConnection)
            }
        }
    }

    fun readFiltersAndMyLikes() {
        if(state.value is MainVMStates.FiltersLoadedSuccessfully || state.value == MainVMStates.ReadingFilters) return
        _state.value = MainVMStates.ReadingFilters
        viewModelScope.launch(IO + coroutineExceptionHandler) {
            readGamesUseCase.readGames()
            readFiltersUseCase.getFilters(this)
            withContext(Main) {
                setNewState(MainVMStates.FiltersLoadedSuccessfully)
                //Stop listen for network changes
                viewModelScope.coroutineContext.job.cancelChildren()
            }

        }
    }

    override fun setNewState(newState: Stateful.State) {
        _state.value = newState as? MainVMStates ?: throw IllegalArgumentException()
        if (newState is Stateful.TerminatingState) resetState()
    }

    override fun resetState() {
        _state.value = MainVMStates.Default
    }
}