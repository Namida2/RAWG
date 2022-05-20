package com.example.featureGameDetails.domain.repositories

import com.example.core.data.games.gameDetailsResponce.GameDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GameDetailsService {
    @GET("games/{id}")
    suspend fun getDetails(@Path("id") id: Int): GameDetailsResponse
}