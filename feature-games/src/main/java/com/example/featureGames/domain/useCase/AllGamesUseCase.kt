package com.example.featureGames.domain.useCase

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.data.models.RAWGame
import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import com.example.featureGames.domain.tools.GameScreens
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AssistedFactory
interface AllGamesFactory {
    fun create(screenTag: GameScreens): AllGamesUseCase
}

class AllGamesUseCase @AssistedInject constructor(
    @Assisted override var screenTag: GameScreens,
    private val applicationContext: Context,
    private val gamesMapper: Game.GameMapper,
    private val gamesHolder: GamesHolder,
    private val requestsQueueGames: RequestQueue<GamesGetRequest, GamesResponse>
) : GamesUseCase, QueueResultHandler<RequestsQueueChanges<GamesResponse>> {

    init {
        requestsQueueGames.onResult = this
    }

    override val newGamesForScreen: Flow<NewGamesForScreen> by gamesHolder::newGamesForScreen
    override val responseHttpExceptions: Flow<HttpException> by requestsQueueGames::responseHttpExceptions
    override val gamesBackgroundImageChanges: Flow<GameBackgroundImageChanges> by gamesHolder::gamesBackgroundImageChanges

    override suspend fun readGames(request: GamesGetRequest, coroutineScope: CoroutineScope) {
        requestsQueueGames.readGames(request, coroutineScope)
    }

    override suspend fun invoke(result: RequestsQueueChanges<GamesResponse>) {
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

    override fun getScreenInfo(): GameScreenInfo =
        gamesHolder.getScreenInfo(screenTag)

    override fun getGamesByPage(page: Int): List<Game> =
        gamesHolder.getGamesByScreenTagAndPage(screenTag, page)

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        requestsQueueGames.onNetworkConnected(coroutineScope)
    }

    private fun readImages(page: Int, response: GamesResponse) {
        val newScope = CoroutineScope(IO)
        response.rawGames?.forEach { game ->
            newScope.launch {
                loadImage(page, game)
            }
        }

    }

    private suspend fun loadImage(page: Int, game: RAWGame) {
        // TODO: Add a ImageLoader class //STOPPED// 
        logD("withContext.loadImage: $page, gameName: ${game.name}")
        game.backgroundImage ?: run {
            logD("background is null for: ${game.name}")
            return
        }
        val bitmap = Glide.with(applicationContext).asBitmap()
            .apply(RequestOptions().override(200, 300))
            .load(game.backgroundImage)
            .submit().get()
        logD("loadImageFromPage: $page")
        logD("bitmap: $bitmap")
        logD("withContext.withContext: $page, gameName: ${game.name}")
        gamesHolder.setBitmapForGameById(screenTag, page, game.id, bitmap)

    }
}
