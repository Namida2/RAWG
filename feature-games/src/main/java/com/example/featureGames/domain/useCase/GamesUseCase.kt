package com.example.featureGames.domain.useCase

import com.example.featureGames.domain.model.Game

interface GamesUseCase {
    suspend fun getGames(): List<Game>
}