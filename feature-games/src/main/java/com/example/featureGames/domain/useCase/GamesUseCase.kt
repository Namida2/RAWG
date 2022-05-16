package com.example.featureGames.domain.useCase

import com.example.core.domain.entities.tools.GameNetworkExceptions
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core_game.domain.GameBackgroundImageChanges
import com.example.core_game.domain.GameScreenInfo
import com.example.core_game.domain.NewGamesForScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface GamesUseCase {
    var screenTag: GameScreenTags
    val newGamesForScreen: Flow<NewGamesForScreen>
    val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges>
    val responseHttpExceptions: Flow<GameNetworkExceptions>
    suspend fun readGames(request: GamesGetRequest)
    fun getScreenInfo(): GameScreenInfo
    fun getGamesByPage(page: Int): List<com.example.core_game.domain.Game>
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}