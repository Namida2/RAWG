package com.example.featureGames.domain.di.modules

import com.example.core.domain.entities.GameNetworkExceptions
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.featureGames.data.entities.rawGameResponse.GamesResponse
import com.example.featureGames.data.requestQueue.GamesRequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import dagger.Binds
import dagger.Module

@Module
interface RequestQueueModule {
    @Binds
    fun provideGamesRequestQueue(
        queue: GamesRequestQueue
    ): RequestQueue<GamesGetRequest, GamesResponse, GameNetworkExceptions>
}