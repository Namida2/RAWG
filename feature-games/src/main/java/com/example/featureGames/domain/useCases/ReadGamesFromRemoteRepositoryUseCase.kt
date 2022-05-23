package com.example.featureGames.domain.useCases

import com.example.core.data.games.rawGameResponse.GamesResponse
import com.example.core.data.games.rawGameResponse.RAWGame
import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.core.data.imageLoaders.interfaces.LoadedImageInfo
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.GameNetworkExceptions
import com.example.core.domain.games.*
import com.example.core.domain.games.interfaces.GameScreenItemType
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueueResultHandler
import com.example.featureGames.data.requestQueue.interfaces.RequestsQueueChanges
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCase
import com.example.featureGames.domain.useCases.interfaces.ReadGamesUseCaseFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AssistedFactory
interface DefaultGamesUseCaseFactory : ReadGamesUseCaseFactory {
    override fun create(
        screenTag: GameScreenTags,
        coroutineScope: CoroutineScope
    ): ReadGamesFromRemoteRepositoryUseCase
}

class ReadGamesFromRemoteRepositoryUseCase @AssistedInject constructor(
    @Assisted override var screenTag: GameScreenTags,
    @Assisted private var coroutineScope: CoroutineScope,
    private val gamesHolder: GamesHolder,
    private val gamesMapper: Game.GameMapper,
    private val imagesLoader: ImagesLoader<GameScreenshotUrlInfo>,
    private val requestsQueueGames: RequestQueue<GamesGetRequest, GamesResponse, GameNetworkExceptions>
) : ReadGamesUseCase, RequestQueueResultHandler<GamesResponse>,
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
            GameScreenItemType.GameType(result.page, games.map { it.gameEntity.id }.toMutableList())
        )
        loadImages(result.page, gamesResponse)
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

    override fun getGamesByPage(page: Int): List<Game> =
        gamesHolder.getGamesByScreenTagAndPage(screenTag, page)

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        requestsQueueGames.onNetworkConnected(coroutineScope)
        imagesLoader.onNetworkConnected(coroutineScope)
    }

    private fun loadImages(page: Int, response: GamesResponse) {
        val newScope = CoroutineScope(IO)
        response.rawGames?.forEach { game ->
            newScope.launch {
                loadImage(page, game)
            }
        }
    }

    private fun loadImage(page: Int, game: RAWGame) {
        val existingGame = gamesHolder.getGameById(game.id)
        if(existingGame.backgroundImage != null || game.backgroundImage == null) return
        imagesLoader.loadImage(
            GameScreenshotUrlInfo(
                game.backgroundImage!!,
                page, game.id
            ), coroutineScope
        )
    }

}
