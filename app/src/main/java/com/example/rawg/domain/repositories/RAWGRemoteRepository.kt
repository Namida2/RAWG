package com.example.rawg.domain.repositories

import com.example.rawg.data.models.Result
import retrofit2.http.GET


interface RAWGRemoteRepository {
    @GET("games?key=eb93975e53414766bd5b734fae1502eb")
    suspend fun getGames(): Result
}