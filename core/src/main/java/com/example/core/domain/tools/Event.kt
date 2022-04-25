package com.example.core.domain.tools

data class Event<T>(val value: T?) {
    private var isHandled = false
    fun getData(): T? = if (isHandled) null else {
        isHandled = true
        value
    }

}