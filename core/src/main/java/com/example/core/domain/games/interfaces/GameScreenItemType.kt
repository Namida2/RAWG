package com.example.core.domain.games.interfaces

import com.example.core.domain.entities.tools.constants.Constants.PAGE_SIZE

sealed interface GameScreenItemType {
    val page: Int

    class GameType(
        override val page: Int, val gameIds: MutableList<Int> = mutableListOf()
    ) : GameScreenItemType

    class GamePlaceHolderType(
        override val page: Int
    ) : GameScreenItemType {
        var placeholderCount = PAGE_SIZE
    }

    class GameErrorPageType(override val page: Int) : GameScreenItemType
}