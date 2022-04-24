package com.example.featureGames.domain.model

import com.example.featureGames.domain.tools.GameScreens

data class GameScreenInfo(
    val tag: GameScreens,
    var request: GamesRequest,
    val gameIds: MutableList<Int> = mutableListOf()
)