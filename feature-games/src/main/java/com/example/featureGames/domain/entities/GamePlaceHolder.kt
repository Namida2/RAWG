package com.example.featureGames.domain.entities

import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType

data class GamePlaceHolder(val id: Int = nextId): BaseRecyclerViewType {
    companion object {
        var nextId: Int = 0
            get() = field++
    }
}