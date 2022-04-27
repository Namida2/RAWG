package com.example.featureGames.domain.model

import android.graphics.Bitmap
import com.example.core.domain.tools.constants.StringConstants.GAME_NOT_FOUND
import com.example.core.domain.tools.constants.StringConstants.GAME_SCREEN_TYPE_MISMATCH
import com.example.core.domain.tools.constants.StringConstants.PAGE_NOT_FOUND
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.domain.tools.TopPicksDameScreenSetting.defaultRequestForTopPicksScreen
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

data class NewGamesForScreen(val screenTag: GameScreens, val page: Int)
data class GameBackgroundImageChanges(val screenTag: GameScreens, val page: Int, val game: Game)

@Singleton
class GamesHolder @Inject constructor() {
    private val games = mutableListOf<Game>()
    private val screensInfo = mutableMapOf<GameScreens, GameScreenInfo>()
    private val _gameScreenChanges = MutableSharedFlow<NewGamesForScreen>(onBufferOverflow = BufferOverflow.SUSPEND)
    val newGamesForScreen: SharedFlow<NewGamesForScreen> = _gameScreenChanges
    private val _gamesBackgroundImageChanges = MutableSharedFlow<GameBackgroundImageChanges>(onBufferOverflow = BufferOverflow.SUSPEND)
    val gamesBackgroundImageChanges: SharedFlow<GameBackgroundImageChanges> = _gamesBackgroundImageChanges

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

    suspend fun setBitmapForGameById(screenTag: GameScreens, page: Int, gameId: Int, bitmap: Bitmap) {
        games.indexOfFirst {
            it.id == gameId
        }.let {
            if (it == -1) throw java.lang.IllegalArgumentException(GAME_NOT_FOUND + gameId)
            //copy to notify the listAdapter about changes
            games[it] = games[it].copy(backgroundImage = bitmap)
            logD("setBitmapForGameById: $page, gameName: ${games[it].name}")
            _gamesBackgroundImageChanges.emit(
                GameBackgroundImageChanges(screenTag, page, games[it])
            )
        }
    }

    suspend fun addGames(
        screenTag: GameScreens,
        newGames: List<Game>,
        gameType: GameScreenItemType.GameType
    ) {
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
        getScreenInfo(screenTag).screenItems[gameType.page] = gameType
        notifyGameScreens(screenTag, gameType.page)
    }

    fun getGamesByScreenTagAndPage(screenTag: GameScreens, page: Int): List<Game> {
        val screenItem = getScreenInfo(screenTag).screenItems[page]
            ?: throw IllegalArgumentException(PAGE_NOT_FOUND + page)
        val gamesScreenItem: GameScreenItemType.GameType =
            screenItem as? GameScreenItemType.GameType
                ?: throw IllegalArgumentException(GAME_SCREEN_TYPE_MISMATCH + page)
        return gamesScreenItem.gameIds.map { gameId ->
            val game = games.find { it.id == gameId }
                ?: throw java.lang.IllegalArgumentException(GAME_NOT_FOUND + gameId); game
        }
    }

    private suspend fun notifyGameScreens(screenTag: GameScreens, page: Int) {
        _gameScreenChanges.emit( NewGamesForScreen(screenTag, page ))
    }

}