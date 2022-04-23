package com.example.featureGames.domain.useCase

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.featureGames.data.models.GamesResponse
import com.example.featureGames.data.models.RAWGame
import com.example.featureGames.domain.model.Game
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllGamesUseCase @Inject constructor(
    private val applicationContext: Context,
    private val gamesService: RAWGamesService,
    private val gamesMapper: Game.GameMapper,
    private val gamesHolder: GamesHolder
) : GamesUseCase {
    override suspend fun readGames(screenTag: String): List<Game> {
        val gamesResponse = gamesService.getGames()
        val games = gamesResponse.rawGames?.map {
            gamesMapper.map(it)
        } ?: return emptyList()
        gamesHolder.addGames(screenTag, games)
        readImages(gamesResponse)
        return games
    }

    // TODO: Load images for games //STOPPED// 
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