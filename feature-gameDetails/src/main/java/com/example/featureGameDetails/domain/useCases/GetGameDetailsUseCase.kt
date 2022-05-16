package com.example.featureGameDetails.domain.useCases

import android.graphics.Bitmap
import com.example.core_game.domain.Game
import com.example.core_game.domain.GamesHolder
import com.example.featureGameDetails.domain.repositories.GameDetailsService
import javax.inject.Inject

class GetGameDetailsUseCaseImpl @Inject constructor(
    private val gameDetailsService: GameDetailsService,
    private val gamesHolder: GamesHolder
): GetGameDetailsUseCase {
    override fun getGameById(gameId: Int): Game =
        gamesHolder.getGameById(gameId)

    override suspend fun getGameDetails(gameId: Int) {
        gameDetailsService.getDevelopers(gameId)
    }

    override fun getScreenShots(gameId: Int): List<Bitmap> {
        TODO("Not yet implemented")
    }
}

interface GetGameDetailsUseCase {
    fun getGameById(gameId: Int): Game
    suspend fun getGameDetails(gameId: Int)
    fun getScreenShots(gameId: Int): List<Bitmap>
}