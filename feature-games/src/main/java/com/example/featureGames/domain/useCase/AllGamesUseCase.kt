package com.example.featureGames.domain.useCase

import com.example.featureGames.domain.model.AllGamesHolder
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.repositories.RAWGamesService
import javax.inject.Inject

class AllGamesUseCase @Inject constructor(
    private val gamesService: RAWGamesService,
    private val gamesMapper: Game.GameMapper,
    private val allGamesHolder: AllGamesHolder
): GamesUseCase {
    // TODO: Map and same the games //STOPPED//
    override suspend fun getGames(): List<Game> {
        val games = gamesService.getGames()
    }
}