package com.example.core.domain.entities.tools

data class SingleEvent<T>(val value: T?) {
    private var isHandled = false
    fun getData(): T? = if (isHandled) null else {
        isHandled = true
        value
    }

}