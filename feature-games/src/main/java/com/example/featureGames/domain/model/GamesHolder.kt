package com.example.featureGames.domain.model

import android.graphics.Bitmap
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.domain.tools.TopPicksDameScreenSetting.defaultRequestForTopPicksScreen
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesHolder @Inject constructor() {
    private val gameNotFountMessage = "Game not found. Id: "
    private val games = mutableListOf<Game>()
    private val screensInfo = mutableMapOf<GameScreens, GameScreenInfo>()

    private val _gameScreenChanges = MutableSharedFlow<GameScreens>(1)
    val gameScreenChanges: SharedFlow<GameScreens> = _gameScreenChanges

    fun getScreenInfo(screenTag: GameScreens): GameScreenInfo =
        screensInfo[screenTag] ?: run {
            val defaultRequest: GamesGetRequest? = when (screenTag) {
                GameScreens.TOP_PICKS -> defaultRequestForTopPicksScreen
                GameScreens.BEST_OF_THE_YER -> null
                GameScreens.NEW_RELEASES -> null
                GameScreens.ALL_GAMES -> null
            }
            GameScreenInfo(screenTag, defaultRequest!!).also {
                screensInfo[screenTag] = it
            }
        }

     fun setBitmapForGameById(gameId: Int, bitmap: Bitmap) {
        games.indexOfFirst {
            it.id == gameId
        }.let {
            if (it == -1) {
                logD(gameNotFountMessage + gameId)
                return
            }
            games[it] = games[it].copy(backgroundImage = bitmap)
            notifyGameScreens(gameId)
        }
    }

    fun addGames(screenTag: GameScreens, newGames: List<Game>) {
        newGames.forEach { newGame ->
            val indexOnExistingGame = games.indexOfFirst { it.id == newGame.id }
            if (indexOnExistingGame == -1) {
                games.add(newGame)
            } else {
                games[indexOnExistingGame] =
                    newGame.copy(isLiked = games[indexOnExistingGame].isLiked)
            }
        }
        logD("gamesCollectionSize: ${games.size}")
        getScreenInfo(screenTag).gameIds.addAll(newGames.map { it.id })
        notifyGameScreens(screenTag)
    }

    fun getGamesBuScreenTag(screenTag: GameScreens): List<Game> {
        val screenGameIds = getScreenInfo(screenTag).gameIds
        return games.filter { game ->
            screenGameIds.contains(game.id)
        }
    }

    private fun notifyGameScreens(gameId: Int) {
        screensInfo.forEach { (tag, info) ->
            if (info.gameIds.contains(gameId))
                _gameScreenChanges.tryEmit(tag)
        }
    }

    private fun notifyGameScreens(screenTag: GameScreens) {
        _gameScreenChanges.tryEmit(screenTag)
    }

}