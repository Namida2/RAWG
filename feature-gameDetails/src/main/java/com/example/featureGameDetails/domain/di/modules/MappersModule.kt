package com.example.featureGameDetails.domain.di.modules

import com.example.core_game.data.gameDetailsResponce.GameDetailsResponse
import com.example.core_game.domain.GameDetails
import dagger.Binds
import dagger.Module

@Module
interface MappersModule {
    @Binds
    fun provideGameDetailsResponseToGameDetailsMapper(
        mapper: GameDetails.GameDetailsMapper
    ): GameDetailsResponse.Mapper<GameDetails>
}