package com.example.featureGames.domain.model

data class GameScreenInfo(
    val tag: String,
    val gameIds: List<Int> = mutableListOf(),
    var lastPosition: Int = 0
)