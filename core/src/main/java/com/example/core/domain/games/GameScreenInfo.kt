package com.example.core.domain.games

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.games.interfaces.GameScreenItemType

data class GameScreenInfo(
    val tag: GameScreenTags,
    var request: GamesGetRequest,
    val screenItems: MutableMap<Int, GameScreenItemType> = mutableMapOf()
) {
    interface Mapper<T> {
        fun mapGameScreenInfo(gameScreenInfo: GameScreenInfo): T
    }
}