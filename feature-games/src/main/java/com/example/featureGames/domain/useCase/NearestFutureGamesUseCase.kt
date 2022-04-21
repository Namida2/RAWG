package com.example.featureGames.domain.useCase

import com.example.featureGames.domain.model.Game
import javax.inject.Inject

class NearestFutureGamesUseCase @Inject constructor() : GamesUseCase {
    override suspend fun getGames(): List<Game> {
        TODO("Not yet implemented")
    }
}