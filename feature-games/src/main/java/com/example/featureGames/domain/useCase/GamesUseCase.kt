package com.example.featureGames.domain.useCase

import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.tools.GameScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

interface GamesUseCase {
    var screenTag: GameScreens
    val newGamesForScreen: Flow<NewGamesForScreen>
    val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges>
    val responseHttpExceptions: Flow<HttpException>
    suspend fun readGames(request: GamesGetRequest,coroutineScope: CoroutineScope)
    fun getScreenInfo(): GameScreenInfo
    fun getGamesByPage(page: Int): List<Game>
}