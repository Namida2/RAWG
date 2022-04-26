package com.example.featureGames.domain.model.interfaces

interface GetRequest {
    fun next(): GetRequest
    fun getPage(): Int
    fun getParams(): Map<String, Any>
}