package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores_for_games", primaryKeys = ["storeId", "gameId"])
data class StoresFoeGames(
    val gameId: Int,
    val storeId: Int,
)