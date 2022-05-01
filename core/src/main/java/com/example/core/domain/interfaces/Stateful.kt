package com.example.core.domain.interfaces

interface Stateful {
    fun setNewState(newState: State)
    fun resetState()
    interface TerminatingState
    interface State
}