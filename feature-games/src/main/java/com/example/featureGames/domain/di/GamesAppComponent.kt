package com.example.featureGames.domain.di

import com.example.featureGames.domain.di.modules.RemoteRepositoryModule
import com.example.featureGames.domain.di.modules.UseCasesImpNames.ALL_GAMES
import com.example.featureGames.domain.di.modules.UseCasesImpNames.NEAREST_FUTURE_GAMES
import com.example.featureGames.domain.di.modules.UseCasesModule
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.GamesUseCase
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GamesAppComponentDeps::class],
    modules = [RemoteRepositoryModule::class, UseCasesModule::class]
)
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
    @Named(ALL_GAMES)
    fun provideAllGamesUseCase(): GamesUseCase
    @Named(NEAREST_FUTURE_GAMES)
    fun provideUseCase(): GamesUseCase
}

interface GamesAppComponentDeps {
    val retrofit: Retrofit
}