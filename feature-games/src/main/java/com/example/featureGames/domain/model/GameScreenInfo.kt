package com.example.featureGames.domain.model

import com.example.featureGames.domain.tools.GameScreens

data class GameScreenInfo(
    val tag: GameScreens,
    var request: GamesGetRequest,
    val gameIds: MutableList<Int> = mutableListOf()
)