package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "platforms_for_games")
data class PlatformsForGames(
    val gameId: Int,
    val platformId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)