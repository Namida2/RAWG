package com.example.core.domain.interfaces.remoteRepository

interface GetRequest {
    val params: Map<String, Any>
    fun next(): GetRequest
    fun getPage(): Int
}