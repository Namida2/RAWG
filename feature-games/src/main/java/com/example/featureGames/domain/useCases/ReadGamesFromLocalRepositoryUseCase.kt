package com.example.featureGames.domain.useCases

import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.core.data.imageLoaders.interfaces.LoadedImageInfo
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.GameNetworkExceptions
import com.example.core.domain.entities.tools.constants.Constants.GAME_SCREENSHOT_HEIGHT
import com.example.core.domain.entities.tools.constants.Constants.GAME_SCREENSHOT_WIDTH
import com.example.core.domain.games.*
import com.example.core.domain.entities.tools.constants.StringConstants
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.enums.ResponseCodes
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCase
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCaseFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

@AssistedFactory
interface MyLikesGamesUseCaseFactory : ReadGamesUseCaseFactory {
    override fun create(
        screenTag: GameScreenTags,
        coroutineScope: CoroutineScope
    ): ReadGamesFromLocalRepositoryUseCase
}

class ReadGamesFromLocalRepositoryUseCase @AssistedInject constructor(
    @Assisted override var screenTag: GameScreenTags,
    @Assisted private var coroutineScope: CoroutineScope,
    private val gamesHolder: GamesHolder,
    private val imagesLoader: ImagesLoader<GameScreenshotUrlInfo>
) : ReadGamesUseCase, ImagesLoaderResultHandler<GameScreenshotUrlInfo> {

    private val _newGamesForScreen = MutableSharedFlow<NewGamesForScreen>(onBufferOverflow = BufferOverflow.SUSPEND)
    override val newGamesForScreen: Flow<NewGamesForScreen> = _newGamesForScreen
    private val _responseHttpExceptions = MutableSharedFlow<GameNetworkExceptions>()
    override val responseHttpExceptions: Flow<GameNetworkExceptions> = _responseHttpExceptions
    override val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges> by gamesHolder::gamesBackgroundImageChanges

    init {
        imagesLoader.onResultHandler = this
        MainScope().launch { gamesHolder.newGamesForScreen.collect { _newGamesForScreen.emit(it) } }
        MainScope().launch { gamesHolder.onGameUnliked.collect {
            _newGamesForScreen.emit(it) }
        }
    }

    override suspend fun readGames(request: GamesGetRequest) {
        try {
            gamesHolder.getGamesByScreenTagAndPage(screenTag, request.getPage())
            gamesHolder.notifyGameScreensAboutNewGames(screenTag, request.getPage())
        } catch (e: Exception) {
            _responseHttpExceptions.emit(
                GameNetworkExceptions.GamesHttpException(
                    HttpException(
                        Response.error<String>(
                            ResponseCodes.PAGE_NOT_FOUND.code,
                            "${StringConstants.PAGE_NOT_FOUND}${request.getPage()}".toResponseBody()
                        )
                    ),
                    request.getPage()
                )
            )
        }
    }

    override fun getScreenInfo(): GameScreenInfo =
        gamesHolder.getScreenInfo(screenTag)

    override fun getGamesByPage(page: Int): List<Game> =
        gamesHolder.getGamesByScreenTagAndPage(screenTag, page).onEach {
            loadImage(page, it)
        }

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        imagesLoader.onNetworkConnected(coroutineScope)
    }

    private fun loadImage(page: Int, game: Game) {
//        logD("withContext.loadImage: $page, gameName: ${game.name}")
        if (game.backgroundImage != null || game.gameEntity.backgroundImageUrl == null) return
        imagesLoader.loadImage(
            GameScreenshotUrlInfo(
                game.gameEntity.backgroundImageUrl!!,
                page, game.gameEntity.id,
                GAME_SCREENSHOT_WIDTH,
                GAME_SCREENSHOT_HEIGHT
            ), coroutineScope
        )
    }

    override suspend fun onImageLoaded(loadedImageInfo: LoadedImageInfo<GameScreenshotUrlInfo>) {
//        logD("loadImageFromPage: ${loadedImageInfo.imageUrlInfo.page}")
//        logD("bitmap: ${loadedImageInfo.bitmap}")
        gamesHolder.setBitmapForGameById(
            screenTag,
            loadedImageInfo.imageUrlInfo.page,
            loadedImageInfo.imageUrlInfo.gameId,
            loadedImageInfo.bitmap
        )
    }

}
