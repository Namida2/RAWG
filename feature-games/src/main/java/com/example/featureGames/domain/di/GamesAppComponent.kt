package com.example.featureGames.domain.di

import android.content.Context
import com.example.featureGames.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGames.domain.di.modules.RequestQueueModule
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.GamesUseCaseFactory
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GamesAppComponentDeps::class],
    modules = [RemoteRepositoriesModule::class, RequestQueueModule::class]
)
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
    fun provideGamesHolder(): GamesHolder
    fun provideAllGamesFactory(): GamesUseCaseFactory
}

interface GamesAppComponentDeps {
    val applicationContext: Context
    val retrofit: Retrofit
}