package com.example.core_game.domain

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core_game.domain.interfaces.GameScreenItemType

data class GameScreenInfo(
    val tag: GameScreenTags,
    var request: GamesGetRequest,
    val screenItems: MutableMap<Int, GameScreenItemType> = mutableMapOf()
) {
    interface Mapper<T> {
        fun mapGameScreenInfo(gameScreenInfo: GameScreenInfo): T
    }
}