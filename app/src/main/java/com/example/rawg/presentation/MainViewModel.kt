package com.example.rawg.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.entities.Message
import com.example.core.domain.entities.NetworkConnectionListener
import com.example.core.domain.interfaces.Stateful
import com.example.core.domain.tools.constants.Messages.defaultErrorMessage
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.rawg.domain.useCases.ReadFiltersUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainVMStates : Stateful.State {
    object Default : MainVMStates()
    object LostNetworkConnection : MainVMStates()
    class Error(
        val message: Message = defaultErrorMessage
    ) : MainVMStates(), Stateful.TerminatingState
    object FiltersLoadedSuccessfully : MainVMStates(), Stateful.TerminatingState
}

class MainViewModel @Inject constructor(
    private val readFiltersUseCase: ReadFiltersUseCase
) : ViewModel(), Stateful {

    private val _state = MutableLiveData<MainVMStates>(MainVMStates.Default)
    val state: LiveData<MainVMStates> = _state
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logE("$this: throwable: $throwable, coroutineContext: $coroutineContext")
            setNewState(MainVMStates.Error())
        }

    init {
        viewModelScope.launch {
            NetworkConnectionListener.networkConnectionChanges.collect { isConnected ->
                if (isConnected) {
                    viewModelScope.coroutineContext.job.cancelChildren()
                } else if(state.value is MainVMStates.LostNetworkConnection) {

                }
            }
        }
    }

    fun readFilters() {
        viewModelScope.launch(IO) {
            readFiltersUseCase.getFilters(this)
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