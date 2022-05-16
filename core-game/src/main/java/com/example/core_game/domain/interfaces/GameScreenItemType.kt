package com.example.core_game.domain.interfaces

import com.example.core.domain.tools.constants.Constants.PAGE_SIZE

sealed interface GameScreenItemType {
    val page: Int
        class GameType(override val page: Int, val gameIds: List<Int> = emptyList()): GameScreenItemType
    class GamePlaceHolderType(override val page: Int): GameScreenItemType {
        var placeholderCount = PAGE_SIZE
    }
    class GameErrorPageType(override val page: Int) : GameScreenItemType
}