package com.example.featureGames.domain.useCase

import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import javax.inject.Inject

class AllGamesUseCase @Inject constructor(
    private val gamesService: RAWGamesService,
    private val gamesMapper: Game.GameMapper,
    private val gamesHolder: GamesHolder
) : GamesUseCase {
    override suspend fun getGames(): List<Game> {
        val games = gamesService.getGames().RAWGames?.map {
            gamesMapper.map(it)
        }
        return games ?: mutableListOf()
    }

    // TODO: Load images for games //STOPPED// 
    private fun readImages(response: GamesResponse) {
        
    }
}