package com.example.featureGames.domain.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesHolder @Inject constructor() {
    private val games = mutableListOf<Game>()
    private val screenInfo = mutableListOf<GameScreenInfo>()

    fun getScreenInfo(tag: String): GameScreenInfo =
        screenInfo.find { it.tag == tag } ?: run { GameScreenInfo(tag) }

}