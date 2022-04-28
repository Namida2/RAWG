package com.example.featureGames.domain.useCase

import com.example.core.domain.entities.GamesHttpException
import com.example.core.domain.entities.HttpExceptionInfo
import com.example.core.domain.tools.enums.GameScreens
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.entities.imageLoader.GameBackgroundImageUrlInfo
import com.example.featureGames.data.entities.rawGameResponse.GamesResponse
import com.example.featureGames.data.entities.rawGameResponse.RAWGame
import com.example.featureGames.data.imageLoaders.LoadedImageInfo
import com.example.featureGames.data.imageLoaders.interfaces.ImagesLoader
import com.example.featureGames.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.featureGames.data.requestQueue.interfaces.RequestQueue
import com.example.featureGames.data.requestQueue.interfaces.RequestQueueResultHandler
import com.example.featureGames.data.requestQueue.interfaces.RequestsQueueChanges
import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AssistedFactory
interface GamesUseCaseFactory {
    fun create(screenTag: GameScreens, coroutineScope: CoroutineScope): AllGamesUseCase
}

class AllGamesUseCase @AssistedInject constructor(
    @Assisted override var screenTag: GameScreens,
    @Assisted private var coroutineScope: CoroutineScope,
    private val gamesHolder: GamesHolder,
    private val gamesMapper: Game.GameMapper,
    private val imagesLoader: ImagesLoader<GameBackgroundImageUrlInfo>,
    private val requestsQueueGames: RequestQueue<GamesGetRequest, GamesResponse, GamesHttpException>
) : GamesUseCase, RequestQueueResultHandler<GamesResponse>,
    ImagesLoaderResultHandler<GameBackgroundImageUrlInfo> {

    init {
        requestsQueueGames.onResultHandler = this
        imagesLoader.onResultHandler = this
    }

    override val newGamesForScreen: Flow<NewGamesForScreen> by gamesHolder::newGamesForScreen
    override val responseHttpExceptions: Flow<GamesHttpException> by requestsQueueGames::responseHttpExceptions
    override val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges> by gamesHolder::gamesBackgroundImageChanges

    override suspend fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope) {
        requestsQueueGames.readGames(request, coroutineScope)
    }

    override suspend fun onResponse(result: RequestsQueueChanges<GamesResponse>) {
        logD("collectPage: ${result.page}")
        logD("responseCount: ${result.response?.count}")
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

    override suspend fun onImageLoaded(loadedImageInfo: LoadedImageInfo<GameBackgroundImageUrlInfo>) {
        logD("loadImageFromPage: ${loadedImageInfo.imageUrlInfo.page}")
        logD("bitmap: ${loadedImageInfo.bitmap}")
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

    private fun readImages(page: Int, response: GamesResponse) {
        val newScope = CoroutineScope(IO)
        response.rawGames?.forEach { game ->
            newScope.launch {
                loadImage(page, game)
            }
        }
    }

    private fun loadImage(page: Int, game: RAWGame) {
        logD("withContext.loadImage: $page, gameName: ${game.name}")
        game.backgroundImage ?: run {
            logD("background is null for: ${game.name}")
            return
        }
        imagesLoader.loadImage(
            GameBackgroundImageUrlInfo(
                game.backgroundImage!!,
                page,
                game.id
            ), coroutineScope
        )
    }

}
