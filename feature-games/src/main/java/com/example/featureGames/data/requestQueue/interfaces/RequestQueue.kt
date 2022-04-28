package com.example.featureGames.data.requestQueue.interfaces

import com.example.core.domain.entities.HttpExceptionInfo
import com.example.featureGames.domain.model.interfaces.GetRequest
import com.example.featureGames.domain.model.interfaces.Response
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.HttpException

data class RequestsQueueChanges<R : Response>(val response: R?, val page: Int)
interface RequestQueueResultHandler<R: Response> {
    suspend fun onResponse(result: RequestsQueueChanges<R>)
}

interface RequestQueue<MyRequest : GetRequest, MyResponse : Response, HttpException: HttpExceptionInfo> {
    val responseHttpExceptions: SharedFlow<HttpException>
    var onResultHandler: RequestQueueResultHandler<MyResponse>
    fun readGames(request: MyRequest, coroutineScope: CoroutineScope)
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}