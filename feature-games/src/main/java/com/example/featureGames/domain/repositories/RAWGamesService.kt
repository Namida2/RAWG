package com.example.featureGames.domain.repositories

import com.example.core_game.data.rawGameResponse.GamesResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RAWGamesService {
    @GET("games")
    @JvmSuppressWildcards
    suspend fun getGames(@QueryMap queryParams: Map<String, Any>? = mapOf()): GamesResponse
}
