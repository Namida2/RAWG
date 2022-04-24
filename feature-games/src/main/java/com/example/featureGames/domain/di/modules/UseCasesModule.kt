package com.example.featureGames.domain.di.modules

import com.example.featureGames.domain.useCase.AllGamesUseCase
import com.example.featureGames.domain.useCase.GamesUseCase
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun provideAllGamesUseCase(useCase: AllGamesUseCase): GamesUseCase

}