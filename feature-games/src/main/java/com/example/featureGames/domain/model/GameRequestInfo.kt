package com.example.featureGames.domain.model

import com.example.featureGames.data.models.GamesResponse

sealed class RequestSates {
    object Completed : RequestSates()
    object InProcess : RequestSates()
    object Failed : RequestSates()
    object Default : RequestSates()
}

data class GameRequestInfo(
    val request: GamesRequest,
    private var response: GamesResponse? = null,
    var state: RequestSates = RequestSates.Default
) {
    fun setResponse(response: GamesResponse) {
        state = RequestSates.Completed
        this.response = response
    }
    fun getResponse() = response
}