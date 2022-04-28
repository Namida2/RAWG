package com.example.featureGames.domain.useCase

import com.example.core.domain.entities.GamesHttpException
import com.example.featureGames.domain.model.*
import com.example.core.domain.tools.enums.GameScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

interface GamesUseCase {
    var screenTag: GameScreens
    val newGamesForScreen: Flow<NewGamesForScreen>
    val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges>
    val responseHttpExceptions: Flow<GamesHttpException>
    suspend fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope)
    fun getScreenInfo(): GameScreenInfo
    fun getGamesByPage(page: Int): List<Game>
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}