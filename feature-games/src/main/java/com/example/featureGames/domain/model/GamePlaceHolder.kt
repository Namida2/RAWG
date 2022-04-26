package com.example.featureGames.domain.model

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class GamePlaceHolder(val id: Int = nextId): BaseRecyclerViewType {
    companion object {
        var nextId: Int = 0
            get() = ++field
    }
}