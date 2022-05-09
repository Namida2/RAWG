package com.example.core.domain.entities

import retrofit2.HttpException
import java.net.SocketTimeoutException

interface NetworkException<T> {
    val exception: T
}

sealed class GameNetworkExceptions {
    data class GamesHttpException(
        override val exception: HttpException, val page: Int
    ) : GameNetworkExceptions(), NetworkException<HttpException>
    data class GameSocketException(
        override val exception: SocketTimeoutException, val page: Int
    ) : GameNetworkExceptions(), NetworkException<SocketTimeoutException>
    data class DefaultPageException(
        override val exception: Exception, val page: Int
    ) : GameNetworkExceptions(), NetworkException<Exception>
}
