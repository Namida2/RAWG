package com.example.featureGames.domain.di

import com.example.featureGames.domain.di.modules.RemoteRepositoryModule
import com.example.featureGames.domain.repositories.RAWGamesService
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(dependencies = [GamesDependencies::class], modules = [RemoteRepositoryModule::class])
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
}

interface GamesDependencies {
    val retrofit: Retrofit
}