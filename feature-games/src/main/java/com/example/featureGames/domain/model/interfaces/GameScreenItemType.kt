package com.example.featureGames.domain.model.interfaces

import com.example.core.domain.tools.Constants.PAGE_SIZE
import com.example.featureGames.domain.model.Game

sealed interface GameScreenItemType {
    val page: Int
        class GameType(override val page: Int, val gameIds: List<Int> = emptyList()): GameScreenItemType
    class GamePlaceHolderType(override val page: Int): GameScreenItemType {
        var placeholderCount = PAGE_SIZE
    }
}