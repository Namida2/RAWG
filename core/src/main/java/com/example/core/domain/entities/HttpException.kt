package com.example.core.domain.entities

import retrofit2.HttpException

interface HttpExceptionInfo {
    val exception: HttpException
}
data class GamesHttpException(override val exception: HttpException, val page: Int): HttpExceptionInfo
