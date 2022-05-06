package com.example.core.domain.interfaces.remoteRepository

interface GetRequest {
    fun next(): GetRequest
    fun getPage(): Int
    fun getParams(): Map<String, Any>
}