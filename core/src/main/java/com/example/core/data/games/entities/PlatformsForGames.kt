package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platforms_for_games", primaryKeys = ["platformId", "gameId"])
data class PlatformsForGames(
    val gameId: Int,
    val platformId: Int,
)