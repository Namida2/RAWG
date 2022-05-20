package com.example.featureGameDetails.domain.di.modules

import com.example.core.data.games.gameDetailsResponce.GameDetailsResponse
import com.example.core.domain.games.GameDetails
import dagger.Binds
import dagger.Module

@Module
interface MappersModule {
    @Binds
    fun provideGameDetailsResponseToGameDetailsMapper(
        mapper: GameDetails.GameDetailsMapper
    ): GameDetailsResponse.Mapper<GameDetails>
}