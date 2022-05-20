package com.example.core.domain.games

import android.graphics.Bitmap
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.games.interfaces.GameScreenItemType
import com.example.core.domain.tools.constants.StringConstants.GAME_NOT_FOUND
import com.example.core.domain.tools.constants.StringConstants.GAME_SCREEN_TYPE_MISMATCH
import com.example.core.domain.tools.constants.StringConstants.PAGE_NOT_FOUND
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.logD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class NewGamesForScreen(val screenTag: GameScreenTags, val page: Int)
data class GameBackgroundImageChanges(
    val screenTag: GameScreenTags,
    val page: Int,
    val game: Game
)

@Singleton
class GamesHolder @Inject constructor() {
    private val games = mutableListOf<Game>()
    private val screensInfo = mutableMapOf<GameScreenTags, GameScreenInfo>()
    private val _gameScreenChanges =
        MutableSharedFlow<NewGamesForScreen>(onBufferOverflow = BufferOverflow.SUSPEND)
    val newGamesForScreen: SharedFlow<NewGamesForScreen> = _gameScreenChanges
    private val _gamesBackgroundImageChanges =
        MutableSharedFlow<GameBackgroundImageChanges>(onBufferOverflow = BufferOverflow.SUSPEND)
    val gamesBackgroundImageChanges: SharedFlow<GameBackgroundImageChanges> =
        _gamesBackgroundImageChanges

    fun getScreenInfo(screenTag: GameScreenTags): GameScreenInfo =
        screensInfo[screenTag] ?: run {
            val defaultRequest: GamesGetRequest = when (screenTag) {
                GameScreenTags.TOP_PICKS -> TopPicksGameScreenSetting.request
                GameScreenTags.NEW_RELEASES -> NewReleasesGameScreenSetting.request
                GameScreenTags.BEST_OF_THE_YER -> BestOfTheYearGameScreenSetting.request
                GameScreenTags.ALL_GAMES -> AllGameScreenSetting.request
                GameScreenTags.MY_LIKES -> MyLikesGameScreenSetting.request
            }
            GameScreenInfo(screenTag, defaultRequest).also {
                screensInfo[screenTag] = it
            }
        }

    suspend fun setBitmapForGameById(
        screenTag: GameScreenTags,
        page: Int,
        gameId: Int,
        bitmap: Bitmap
    ) {
        games.indexOfFirst {
            it.gameEntity.id == gameId
        }.let {
            if (it == -1) throw IllegalArgumentException(GAME_NOT_FOUND + gameId)
            //copy to notify a listAdapter about changes
            games[it] = games[it].copy(backgroundImage = bitmap)
            _gamesBackgroundImageChanges.emit(
                GameBackgroundImageChanges(screenTag, page, games[it])
            )
        }
    }

    fun addShortGameScreenshot(gameId: Int, screenshotUrl: String, shortScreenshot: Bitmap) {
        games.indexOfFirst {
            it.gameEntity.id == gameId
        }.let {
            if (it == -1) throw IllegalArgumentException(GAME_NOT_FOUND + gameId)
            games[it].shortScreenshots?.set(screenshotUrl, (shortScreenshot))
            logD("addShortGameScreenshot: gameName: ${games[it].gameEntity.name}")
        }
    }

    fun changeGameLikeStatus(game: Game, isLiked: Boolean) {
        games.indexOfFirst {
            it.gameEntity.id == game.gameEntity.id
        }.let {
            if (it == -1) throw IllegalArgumentException(GAME_NOT_FOUND + game.gameEntity.id)
            games[it] = game.copy(gameEntity = games[it].gameEntity.copy(isLiked = isLiked))
            notifyGameChanged(game)
        }
    }

    suspend fun addGames(
        screenTag: GameScreenTags,
        newGames: List<Game>,
        gameType: GameScreenItemType.GameType
    ) {
        newGames.forEach { newGame ->
            val indexOnExistingGame =
                games.indexOfFirst { it.gameEntity.id == newGame.gameEntity.id }
            if (indexOnExistingGame == -1) {
                games.add(newGame)
            } else {
                games[indexOnExistingGame] =
                    newGame.copy(gameEntity = games[indexOnExistingGame].gameEntity.copy(isLiked = games[indexOnExistingGame].gameEntity.isLiked))
            }
        }
//        logD("gamesCollectionSize: ${games.size}")
        getScreenInfo(screenTag).screenItems[gameType.page] = gameType
        notifyGameScreensAboutNewGames(screenTag, gameType.page)
    }

    fun getGamesByScreenTagAndPage(
        screenTag: GameScreenTags,
        page: Int
    ): List<Game> {
        val screenItem = getScreenInfo(screenTag).screenItems[page]
            ?: throw IllegalArgumentException(PAGE_NOT_FOUND + page)
        val gamesScreenItem: GameScreenItemType.GameType =
            screenItem as? GameScreenItemType.GameType
                ?: throw IllegalArgumentException(GAME_SCREEN_TYPE_MISMATCH + page)
        return gamesScreenItem.gameIds.map { gameId ->
            val game = games.find { it.gameEntity.id == gameId }
                ?: throw java.lang.IllegalArgumentException(GAME_NOT_FOUND + gameId); game
        }
    }

    fun getGameById(gameId: Int): Game =
        games.find { it.gameEntity.id == gameId }
            ?: throw IllegalArgumentException(GAME_NOT_FOUND + gameId)

    private suspend fun notifyGameScreensAboutNewGames(screenTag: GameScreenTags, page: Int) {
        _gameScreenChanges.emit(NewGamesForScreen(screenTag, page))
    }

    private fun notifyGameChanged(newGame: Game) {
        screensInfo.values.forEach { gameScreenInfo ->
            if (gameScreenInfo.screenItems.values.first() is GameScreenItemType.GameType) {
                gameScreenInfo.screenItems.forEach { page, gameScreenItem ->
                    if ((gameScreenItem as GameScreenItemType.GameType).gameIds.contains(newGame.gameEntity.id))
                        MainScope().launch { notifyGameScreensAboutNewGames(gameScreenInfo.tag, page) }
                }
            }
        }
    }

}