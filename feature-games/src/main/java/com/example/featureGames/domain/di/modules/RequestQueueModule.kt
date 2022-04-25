package com.example.featureGames.domain.di.modules

import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.domain.model.GamesGetRequest
import com.example.featureGames.domain.model.GamesRequestQueue
import com.example.featureGames.domain.model.RequestQueue
import dagger.Binds
import dagger.Module

@Module
interface RequestQueueModule {
    @Binds
    fun provideGamesRequestQueue(queue: GamesRequestQueue): RequestQueue<GamesGetRequest, GamesResponse>
}