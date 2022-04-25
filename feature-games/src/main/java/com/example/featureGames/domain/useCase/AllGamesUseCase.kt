package com.example.featureGames.domain.useCase

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.data.models.RAWGame
import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.tools.GameScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AllGamesUseCase @Inject constructor(
    private val applicationContext: Context,
    private val gamesMapper: Game.GameMapper,
    private val gamesHolder: GamesHolder,
    private val requestsQueueGames: RequestQueue<GamesGetRequest, GamesResponse>
) : GamesUseCase {

    override val gameScreenChanges: Flow<GameScreens> by gamesHolder::gameScreenChanges
    override suspend fun readGames(screenTag: GameScreens, request: GamesGetRequest) {
        requestsQueueGames.readGames(request)
        if (requestsQueueGames.onResult == null) {
            requestsQueueGames.onResult =
                object : QueueResult<RequestsQueueChanges<GamesResponse>> {
                    override fun invoke(it: RequestsQueueChanges<GamesResponse>) {
                        logD("collectPage: ${it.page}")
                        logD("responseCount: ${it.response?.count}")
                        val gamesResponse = it.response ?: return
                        val games = it.response.rawGames?.map { game ->
                            gamesMapper.map(game)
                        } ?: return
                        logD("gamesCount: ${games.size}")
                        gamesHolder.addGames(screenTag, games)
                        readImages(gamesResponse)
                    }
                }
        }
    }

    override fun getScreenInfo(screenTag: GameScreens): GameScreenInfo =
        gamesHolder.getScreenInfo(screenTag)

    override fun getGamesBuScreenTag(screenTag: GameScreens): List<Game> =
        gamesHolder.getGamesBuScreenTag(screenTag)

    private fun readImages(response: GamesResponse) {
        response.rawGames?.forEach { game ->
            CoroutineScope(IO).launch {
                loadImage(game)
            }
        }
    }

    private suspend fun loadImage(game: RAWGame) {
        game.backgroundImage ?: return
        val bitmap = Glide.with(applicationContext).asBitmap()
            .apply(RequestOptions().override(120, 220))
            .load(game.backgroundImage).submit()
            .get()
        withContext(Main) {
            gamesHolder.setBitmapForGameById(game.id, bitmap)
        }
    }
}
