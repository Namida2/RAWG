package com.example.featureGames.domain.useCase

import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.core.data.imageLoaders.interfaces.LoadedImageInfo
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.GameNetworkExceptions
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.logD
import com.example.core_game.data.rawGameResponse.GamesResponse
import com.example.core_game.data.rawGameResponse.RAWGame
import com.example.core_game.domain.GameBackgroundImageChanges
import com.example.core_game.domain.GameScreenInfo
import com.example.core_game.domain.GamesHolder
import com.example.core_game.domain.NewGamesForScreen
import com.example.core_game.domain.interfaces.GameScreenItemType
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueueResultHandler
import com.example.featureGames.data.requestQueue.interfaces.RequestsQueueChanges
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AssistedFactory
interface GamesUseCaseFactory {
    fun create(screenTag: GameScreenTags, coroutineScope: CoroutineScope): AllGamesUseCase
}

class AllGamesUseCase @AssistedInject constructor(
    @Assisted override var screenTag: GameScreenTags,
    @Assisted private var coroutineScope: CoroutineScope,
    private val gamesHolder: GamesHolder,
    private val gamesMapper: com.example.core_game.domain.Game.GameMapper,
    private val imagesLoader: ImagesLoader<GameScreenshotUrlInfo>,
    private val requestsQueueGames: RequestQueue<GamesGetRequest, GamesResponse, GameNetworkExceptions>
) : GamesUseCase, RequestQueueResultHandler<GamesResponse>,
    ImagesLoaderResultHandler<GameScreenshotUrlInfo> {

    init {
        requestsQueueGames.onResultHandler = this
        imagesLoader.onResultHandler = this
    }

    override val newGamesForScreen: Flow<NewGamesForScreen> by gamesHolder::newGamesForScreen
    override val responseHttpExceptions: Flow<GameNetworkExceptions> by requestsQueueGames::onNetworkExceptions
    override val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges> by gamesHolder::gamesBackgroundImageChanges

    override suspend fun readGames(request: GamesGetRequest) {
        requestsQueueGames.readGames(request, coroutineScope)
    }

    override suspend fun onResponse(result: RequestsQueueChanges<GamesResponse>) {
        logD("collectPage: ${result.page}")
//        logD("responseCount: ${result.response?.count}")
        val gamesResponse = result.response ?: return
        val games = result.response.rawGames?.map { game ->
            gamesMapper.map(game)
        } ?: return
        logD("gamesCount: ${games.size}")
        gamesHolder.addGames(
            screenTag, games,
            GameScreenItemType.GameType(result.page, games.map { it.id })
        )
        readImages(result.page, gamesResponse)
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

    override fun getScreenInfo(): GameScreenInfo =
        gamesHolder.getScreenInfo(screenTag)

    override fun getGamesByPage(page: Int): List<com.example.core_game.domain.Game> =
        gamesHolder.getGamesByScreenTagAndPage(screenTag, page)

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        requestsQueueGames.onNetworkConnected(coroutineScope)
        imagesLoader.onNetworkConnected(coroutineScope)
    }

    private fun readImages(page: Int, response: GamesResponse) {
        val newScope = CoroutineScope(IO)
        response.rawGames?.forEach { game ->
            newScope.launch {
                loadImage(page, game)
            }
        }
    }

    private fun loadImage(page: Int, game: RAWGame) {
//        logD("withContext.loadImage: $page, gameName: ${game.name}")
        game.backgroundImage ?: run {
            logD("background is null for: ${game.name}")
            return
        }
        imagesLoader.loadImage(
            GameScreenshotUrlInfo(
                game.backgroundImage!!,
                page,
                game.id
            ), coroutineScope
        )
    }

}
