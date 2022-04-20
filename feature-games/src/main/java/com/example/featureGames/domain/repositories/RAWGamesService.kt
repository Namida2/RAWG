package com.example.featureGames.domain.repositories

import com.example.featureGames.data.models.ResponseGame
import retrofit2.http.GET

interface RAWGamesService {
    @GET("games")
    suspend fun getGames(): ResponseGame
}