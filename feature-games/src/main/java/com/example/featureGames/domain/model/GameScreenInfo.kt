package com.example.featureGames.domain.model

import com.example.featureGames.domain.model.interfaces.GameScreenItemType
import com.example.core.domain.tools.enums.GameScreenTags

data class GameScreenInfo(
    val tag: GameScreenTags,
    var request: GamesGetRequest,
    val screenItems: MutableMap<Int, GameScreenItemType> = mutableMapOf()
) {
    interface Mapper<T> {
        fun mapGameScreenInfo(gameScreenInfo: GameScreenInfo): T
    }
}