package com.example.featureGames.domain.di

import android.content.Context
import com.example.featureGames.domain.di.modules.RemoteRepositoryModule
import com.example.featureGames.domain.di.modules.RequestQueueModule
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.AllGamesFactory
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GamesAppComponentDeps::class],
    modules = [RemoteRepositoryModule::class, RequestQueueModule::class]
)
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
    fun provideGamesHolder(): GamesHolder
    fun provideAllGamesFactory(): AllGamesFactory
}

interface GamesAppComponentDeps {
    val applicationContext: Context
    val retrofit: Retrofit
}