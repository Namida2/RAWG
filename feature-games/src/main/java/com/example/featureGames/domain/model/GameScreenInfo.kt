package com.example.featureGames.domain.model

import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import com.example.core.domain.tools.enums.GameScreens

data class GameScreenInfo(
    val tag: GameScreens,
    var request: GamesGetRequest,
    val screenItems: MutableMap<Int, GameScreenItemType> = mutableMapOf()
)