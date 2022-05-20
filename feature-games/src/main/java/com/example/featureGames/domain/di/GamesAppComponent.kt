package com.example.featureGames.domain.di

import com.example.core.domain.di.modules.DatabaseModule
import com.example.core.domain.di.modules.UseCasesModule
import com.example.dependencyDescription.domain.FeatureGamesDeps
import com.example.featureGames.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGames.domain.di.modules.RequestQueueModule
import com.example.core.domain.games.GamesHolder
import com.example.core.domain.games.useCases.LikeGameUseCase
import com.example.dependencyDescription.domain.GameDetailsDeps
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.GamesUseCaseFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [FeatureGamesDeps::class],
    modules = [RemoteRepositoriesModule::class, RequestQueueModule::class, UseCasesModule::class, DatabaseModule::class]
)
interface GamesAppComponent: GameDetailsDeps {
    fun provideRAWGamesService(): RAWGamesService
    fun provideGamesHolder(): GamesHolder
    fun provideAllGamesFactory(): GamesUseCaseFactory
    fun provideLikeGameUseCase(): LikeGameUseCase
}
