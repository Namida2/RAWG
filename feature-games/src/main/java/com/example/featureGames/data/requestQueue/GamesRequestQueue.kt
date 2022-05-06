package com.example.featureGames.data.requestQueue

import com.example.core.domain.entities.GamesHttpException
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.entities.rawGameResponse.GamesResponse
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueueResultHandler
import com.example.featureGames.data.requestQueue.interfaces.RequestsQueueChanges
import com.example.featureGames.domain.model.GameRequestInfo
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.featureGames.domain.repositories.RAWGamesService
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

class GamesRequestQueue @Inject constructor(
    private val gamesService: RAWGamesService
) : RequestQueue<GamesGetRequest, GamesResponse, GamesHttpException> {
    private val requests = Collections.synchronizedMap(mutableMapOf<Int, GameRequestInfo>())
    override lateinit var onResultHandler: RequestQueueResultHandler<GamesResponse>
    private val _responseHttpExceptions =
        MutableSharedFlow<GamesHttpException>(onBufferOverflow = BufferOverflow.SUSPEND)
    override val responseHttpExceptions: SharedFlow<GamesHttpException> =
        _responseHttpExceptions
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logD("$this: coroutineContext: $coroutineContext, throwable: $throwable")
        }
    private val defaultContext = Job() + Main.immediate

    override fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope) {
        requests[request.getPage()] = GameRequestInfo(request)
        makeRequest(request, coroutineScope)
    }

    private fun makeRequest(request: GamesGetRequest, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            try {
                requests[request.getPage()]?.setResponse(gamesService.getGames(request.getParams()))
                //Use a new job to avoid cancellation of passed coroutineContext's job
                // when it is suspended in sharedFlow after using emit
                withContext(defaultContext) {
                    onRequestComplete(request)
                }
            } catch (e: Exception) {
                logD("requests in exception: ${requests.keys}")
                coroutineExceptionHandler.handleException(coroutineContext, e)
                (e as? HttpException)?.let { onException(GamesHttpException(e, request.getPage())) }
            }
        }
    }

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        //Use the defaultContext thread to avoid the concurrentModificationException
        // when trying to remove a request from map
        coroutineScope.launch(defaultContext) {
            requests.forEach { (_, requestInfo) ->
                makeRequest(requestInfo.request, coroutineScope)
            }
        }
    }

    private fun onException(exceptionInfo: GamesHttpException) {
        MainScope().launch {
            requests.remove(exceptionInfo.page)
            _responseHttpExceptions.emit(exceptionInfo)
        }
    }

    private suspend fun onRequestComplete(request: GamesGetRequest) {
//        logD("onRequestComplete: page: ${request.getPage()}, state: ${requests[request.getPage()]?.state}")
        val response = requests[request.getPage()]
//        logD("requests: " + requests.keys.toString())
        requests.remove(request.getPage())
//        logD("page: " + request.getPage().toString())
//        logD("requests: ${requests.keys}")
//        logD("-------emit-------")
        onResultHandler.onResponse(
            RequestsQueueChanges(response?.getResponse(), request.getPage())
        )
    }

}