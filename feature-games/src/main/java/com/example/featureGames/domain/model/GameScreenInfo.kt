package com.example.featureGames.domain.model

data class GameScreenInfo(
    val tag: String,
    val gameIds: MutableList<Int> = mutableListOf()
)