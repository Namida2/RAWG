package com.example.core.domain.tools.interfaces

interface Stateful {
    fun setNewState(newState: State)
    fun resetState()
    interface TerminatingState
    interface State
}