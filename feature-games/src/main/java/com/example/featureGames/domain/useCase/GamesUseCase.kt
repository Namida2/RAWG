package com.example.featureGames.domain.useCase

import com.example.core.domain.entities.GamesHttpException
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.featureGames.domain.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface GamesUseCase {
    var screenTag: GameScreenTags
    val newGamesForScreen: Flow<NewGamesForScreen>
    val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges>
    val responseHttpExceptions: Flow<GamesHttpException>
    suspend fun readGames(request: GamesGetRequest)
    fun getScreenInfo(): GameScreenInfo
    fun getGamesByPage(page: Int): List<Game>
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}