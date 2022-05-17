package com.example.featureGameDetails.domain.repositories

import com.example.core_game.data.gameDetailsResponce.GameDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface GameDetailsService {
    @GET("games/{id}")
    suspend fun getDevelopers(@Path("id") id: Int): GameDetailsResponse
}