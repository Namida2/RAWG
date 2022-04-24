package com.example.featureGames.domain.useCase

import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GameScreenInfo
import com.example.featureGames.domain.model.GamesRequest
import com.example.featureGames.domain.tools.GameScreens
import kotlinx.coroutines.flow.Flow

interface GamesUseCase {
    val gameScreenChanges: Flow<GameScreens>
    suspend fun readGames(screenTag: GameScreens, request: GamesRequest): List<Game>
    fun getScreenInfo(screenTag: GameScreens): GameScreenInfo
    fun getGamesBuScreenTag(screenTag: GameScreens): List<Game>
}