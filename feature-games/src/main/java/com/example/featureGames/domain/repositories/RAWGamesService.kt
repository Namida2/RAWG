package com.example.featureGames.domain.repositories

import com.example.featureGames.data.models.ResponseGame
import com.example.featureGames.domain.model.Game
import retrofit2.http.GET

interface RAWGamesService {
    @GET("games")
    suspend fun getGames(): ResponseGame
}
