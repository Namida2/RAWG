package com.example.featureGames.domain.useCase

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.data.models.RAWGame
import com.example.featureGames.domain.model.*
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.tools.GameScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Collections.synchronizedMap
import javax.inject.Inject

data class RequestsQueueChanges(val response: GamesResponse?, val isLastResponse: Boolean)

class AllGamesUseCase @Inject constructor(
    private val applicationContext: Context,
    private val gamesMapper: Game.GameMapper,
    private val gamesHolder: GamesHolder,
    private val requestsQueue: RequestsQueue
) : GamesUseCase {

    class RequestsQueue @Inject constructor(
        private val gamesService: RAWGamesService,
    ) {
        private var minRequestPage = 1 //min page size
        private val requests = synchronizedMap(mutableMapOf<Int, GameRequestInfo>())

        // TODO: Complete the pagination //STOPPED//
        private val _changes = MutableSharedFlow<RequestsQueueChanges>(replay = 10, extraBufferCapacity = 10)
        val changes: SharedFlow<RequestsQueueChanges> = _changes

        fun readGames(request: GamesRequest) {
            if (requests.isEmpty()) minRequestPage = request.getPage()
            requests[request.getPage()] = GameRequestInfo(request)
            CoroutineScope(IO).launch {
                requests[request.getPage()]?.setResponse(gamesService.getGames(request.getParams()))
//                withContext(Main) {
                    onRequestComplete(request)
//                }
            }
        }

        private fun onRequestComplete(request: GamesRequest) {
            if (minRequestPage == request.getPage()) {
                val response = requests[request.getPage()]
                logD(response.toString())
                logD(requests.toString())
                logD("page: " + request.getPage().toString())
                requests.remove(request.getPage())
                _changes.tryEmit(
                    RequestsQueueChanges(response?.getResponse(), requests.isEmpty())
                )
                minRequestPage = requests.keys.toHashSet().firstOrNull() ?: return
                logD("minRequestPage: $minRequestPage")
                if(requests[minRequestPage]?.state == RequestSates.Completed) {
                    logD("minRequestPage: $minRequestPage Completed" )
                    onRequestComplete(requests[minRequestPage]!!.request)
                }
            }
        }
    }

    override val gameScreenChanges: Flow<GameScreens> by gamesHolder::gameScreenChanges

    override suspend fun readGames(screenTag: GameScreens, request: GamesRequest): List<Game> {
        requestsQueue.readGames(request)
        requestsQueue.readGames(request.copy().incrementPage())
        requestsQueue.readGames(request.copy().incrementPage().incrementPage())
        requestsQueue.readGames(request.copy().incrementPage().incrementPage().incrementPage())
        requestsQueue.changes.collect {
            logD("collect")
            logD(it.toString())
            val gamesResponse = it.response ?: return@collect
            val games = it.response.rawGames?.map {
                gamesMapper.map(it)
            } ?: return@collect
            logD(games.toString())
            gamesHolder.addGames(screenTag, games)
            readImages(gamesResponse)
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

    private fun loadImage(game: RAWGame) {
        gamesHolder.setBitmapForGameById(
            game.id,
            Glide.with(applicationContext).asBitmap()
                .apply(RequestOptions().override(200, 300))
                .load(game.backgroundImage).submit()
                .get()
        )
    }
}
