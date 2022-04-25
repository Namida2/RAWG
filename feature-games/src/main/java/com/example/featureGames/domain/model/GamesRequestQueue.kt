package com.example.featureGames.domain.model

import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.domain.repositories.RAWGamesService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException
import java.lang.Exception
import java.security.cert.Extension
import java.util.*
import javax.inject.Inject

data class RequestsQueueChanges<R: Response>(val response: R?, val page: Int)
typealias QueueResult<R> = (R) -> Unit
interface RequestQueue<MyRequest: GetRequest, MyResponse: Response> {
    var onResult: QueueResult<RequestsQueueChanges<GamesResponse>>?
    fun readGames(request: GamesGetRequest)
}

class GamesRequestQueue @Inject constructor(
    private val gamesService: RAWGamesService,
): RequestQueue<GamesGetRequest, GamesResponse> {
    private var minRequestPage = 1
    private val requests = Collections.synchronizedMap(mutableMapOf<Int, GameRequestInfo>())
    override var onResult: QueueResult<RequestsQueueChanges<GamesResponse>>? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun readGames(request: GamesGetRequest) {
        if (requests.isEmpty() || request.getPage() < minRequestPage)
            minRequestPage = request.getPage()
        requests[request.getPage()] = GameRequestInfo(request)
        // TODO: Add coroutine exception handler //STOPPED//
        CoroutineScope(Dispatchers.IO).launch {
            try {
                requests[request.getPage()]?.setResponse(gamesService.getGames(request.getParams()))
            } catch (e: HttpException) {
                e.printStackTrace()
                logD("code: ${e.code()}")
            }
            //Go to the Main thread to use tryEmit synchronized
            withContext(Dispatchers.Main) {
                onRequestComplete(request)
            }
        }
    }

    private fun onRequestComplete(request: GamesGetRequest) {
        //To avoid race condition
        if(requests.isEmpty()) return
        if (minRequestPage == request.getPage()) {
            logD("onRequestComplete: page: ${request.getPage()}, state: ${requests[request.getPage()]?.state}")
            val response = requests[request.getPage()]
            logD("requests: " + requests.keys.toString())
            requests.remove(request.getPage())
            logD("page: " + request.getPage().toString())
            logD("-------emit-------")
            onResult!!.invoke(
                RequestsQueueChanges(response?.getResponse(), request.getPage())
            )
            minRequestPage = requests.keys.minOrNull() ?: return
            logD("minRequestPage: $minRequestPage")
            if (requests[minRequestPage]?.state == RequestSates.Completed) {
                logD("minRequestPage: $minRequestPage Completed")
                onRequestComplete(requests[minRequestPage]!!.request)
            }
        }
    }

}