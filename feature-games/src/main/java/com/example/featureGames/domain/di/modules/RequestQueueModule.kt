package com.example.featureGames.domain.di.modules

import com.example.core.domain.entities.GamesHttpException
import com.example.featureGames.data.entities.rawGameResponse.GamesResponse
import com.example.featureGames.data.requestQueue.GamesRequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import com.example.featureGames.domain.model.GamesGetRequest
import dagger.Binds
import dagger.Module

@Module
interface RequestQueueModule {
    @Binds
    fun provideGamesRequestQueue(
        queue: GamesRequestQueue
    ): RequestQueue<GamesGetRequest, GamesResponse, GamesHttpException>
}