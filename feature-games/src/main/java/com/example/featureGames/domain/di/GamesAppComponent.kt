package com.example.featureGames.domain.di

import com.example.dependencyDescription.domain.FeatureGamesDeps
import com.example.featureGames.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGames.domain.di.modules.RequestQueueModule
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.GamesUseCaseFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [FeatureGamesDeps::class],
    modules = [RemoteRepositoriesModule::class, RequestQueueModule::class]
)
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
    fun provideGamesHolder(): GamesHolder
    fun provideAllGamesFactory(): GamesUseCaseFactory
}
