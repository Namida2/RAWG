package com.example.featureGames.domain.model

import com.example.core.domain.tools.Constants.FIRST_PAGE
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.domain.model.interfaces.GetRequest
import com.example.featureGames.domain.model.interfaces.Response
import com.example.featureGames.domain.repositories.RAWGamesService
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

data class RequestsQueueChanges<R : Response>(val response: R?, val page: Int)
typealias QueueResultHandler<R> = (R) -> Unit

interface RequestQueue<MyRequest : GetRequest, MyResponse : Response> {
    val responseHttpExceptions: SharedFlow<HttpException>
    var onResult: QueueResultHandler<RequestsQueueChanges<GamesResponse>>?
    fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope)
}

class GamesRequestQueue @Inject constructor(
    private val gamesService: RAWGamesService
) : RequestQueue<GamesGetRequest, GamesResponse> {
//    private var minRequestPage = FIRST_PAGE
    private val requests = Collections.synchronizedMap(mutableMapOf<Int, GameRequestInfo>())
    override var onResult: QueueResultHandler<RequestsQueueChanges<GamesResponse>>? = null
    private val _responseHttpExceptions = MutableSharedFlow<HttpException>(replay = 1)
    override val responseHttpExceptions: SharedFlow<HttpException> = _responseHttpExceptions
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            MainScope().launch {
                (throwable as? HttpException)?.let { _responseHttpExceptions.tryEmit(it) }
            }
            throwable.printStackTrace()
            logD("coroutineContext: $coroutineContext, throwable: $throwable")
        }

    // TODO: Check the order of requests
    override fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope) {
//        if (requests.isEmpty() || request.getPage() < minRequestPage) minRequestPage = request.getPage()
        requests[request.getPage()] = GameRequestInfo(request)
        coroutineScope.launch(coroutineExceptionHandler) {
            val response = gamesService.getGames(request.getParams())
            requests[request.getPage()]?.setResponse(response)
            //Go to the Main thread to use tryEmit synchronized
            withContext(Main) {
                onRequestComplete(request)
            }
        }
    }

    private fun onRequestComplete(request: GamesGetRequest) {
        //To avoid race condition
        if (requests.isEmpty()) return
//        if (minRequestPage == request.getPage()) {
            logD("onRequestComplete: page: ${request.getPage()}, state: ${requests[request.getPage()]?.state}")
            val response = requests[request.getPage()]
            logD("requests: " + requests.keys.toString())
            requests.remove(request.getPage())
            logD("page: " + request.getPage().toString())
            logD("-------emit-------")
            onResult!!.invoke(
                RequestsQueueChanges(response?.getResponse(), request.getPage())
            )
//            minRequestPage = requests.keys.minOrNull() ?: return
//            logD("minRequestPage: $minRequestPage")
//            if (requests[minRequestPage]?.state == RequestSates.Completed) {
//                logD("minRequestPage: $minRequestPage Completed")
//                onRequestComplete(requests[minRequestPage]!!.request)
//            }
        }
//    }

}