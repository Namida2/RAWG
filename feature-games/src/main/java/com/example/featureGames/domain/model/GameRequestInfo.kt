package com.example.featureGames.domain.model

import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.domain.model.interfaces.GetRequest
import com.example.featureGames.domain.model.interfaces.Response

sealed class RequestSates {
    object Completed : RequestSates()
    object InProcess : RequestSates()
    object Failed : RequestSates()
    object Default : RequestSates()
}

data class GameRequestInfo(
    override val request: GamesGetRequest,
    private var response: GamesResponse? = null,
    override var state: RequestSates = RequestSates.Default
): RequestInfo<GamesGetRequest, GamesResponse?>{
    override fun setResponse(response: GamesResponse?) {
        state = RequestSates.Completed
        this.response = response
    }
    override fun getResponse(): GamesResponse? = response
}

interface RequestInfo<MyRequest: GetRequest, MyResponse: Response?> {
    val request: MyRequest
    var state: RequestSates
    fun setResponse(response: MyResponse)
    fun getResponse(): MyResponse
}
