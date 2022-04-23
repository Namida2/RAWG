package com.example.featureGames.domain.model

import android.graphics.Bitmap
import com.example.core.domain.tools.extensions.logD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesHolder @Inject constructor() {
    private val gameNotFountMessage = "Game not found. Id: "
    private val games = mutableListOf<Game>()
    private val screensInfo = mutableMapOf<String, GameScreenInfo>()

    private val _gameScreenChanges = MutableSharedFlow<String>(1)
    val gameScreenChanges: SharedFlow<String> = _gameScreenChanges

    private fun getScreenInfo(tag: String): GameScreenInfo =
        screensInfo[tag] ?: run { GameScreenInfo(tag).also {
            screensInfo[tag] = it
        } }

    fun setBitmapForGameById(gameId: Int, bitmap: Bitmap) {
        games.indexOfFirst {
            it.id == gameId
        }.let {
            if(it == -1) {
                logD(gameNotFountMessage + gameId)
                return
            }
            games[it] = games[it].copy(backgroundImage = bitmap)
            notifyGameScreens(gameId)
        }
    }

    fun addGames(screenTag: String, newGames: List<Game>) {
        games.removeIf { game ->
            newGames.find { it.id == game.id} != null
        }
        getScreenInfo(screenTag).gameIds.addAll(newGames.map { it.id })
        games.addAll(newGames)
    }

    fun getGamesBuScreenTag(screenTag: String): List<Game>  {
        val screenGameIds = getScreenInfo(screenTag).gameIds
        return games.filter { game ->
            screenGameIds.contains(game.id)
        }
    }

    private fun notifyGameScreens(gameId: Int) {
        screensInfo.forEach { (tag, info) ->
            if(info.gameIds.contains(gameId))
                _gameScreenChanges.tryEmit(tag)
        }
    }

}