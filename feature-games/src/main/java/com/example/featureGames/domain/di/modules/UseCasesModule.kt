package com.example.featureGames.domain.di.modules

import com.example.featureGames.domain.di.modules.UseCasesImpNames.ALL_GAMES
import com.example.featureGames.domain.di.modules.UseCasesImpNames.NEAREST_FUTURE_GAMES
import com.example.featureGames.domain.useCase.AllGamesUseCase
import com.example.featureGames.domain.useCase.GamesUseCase
import com.example.featureGames.domain.useCase.NearestFutureGamesUseCase
import dagger.Binds
import dagger.Module
import javax.inject.Named

object UseCasesImpNames {
    const val ALL_GAMES = "allGames"
    const val NEAREST_FUTURE_GAMES = "nearestFutureGames"
}

@Module
interface UseCasesModule {
    @Binds
    @Named(ALL_GAMES)
    fun provideAllGamesUseCase(useCase: AllGamesUseCase): GamesUseCase

    @Binds
    @Named(NEAREST_FUTURE_GAMES)
    fun provideUseCase(useCase: NearestFutureGamesUseCase): GamesUseCase
}