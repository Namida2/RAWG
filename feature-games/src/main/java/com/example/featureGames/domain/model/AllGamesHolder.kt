package com.example.featureGames.domain.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AllGamesHolder @Inject constructor() {
    private val allGames = mutableListOf<Game>()
}