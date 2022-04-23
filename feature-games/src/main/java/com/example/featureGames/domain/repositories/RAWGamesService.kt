package com.example.featureGames.domain.repositories

import com.example.featureGames.data.models.GamesResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RAWGamesService {
    @GET("games?dates=2022-01-01,2025-01-01")
    suspend fun getGames(@QueryMap options: Map<String, String>? = mapOf()): GamesResponse
}
