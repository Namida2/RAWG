package com.example.core.data.games.entities

import androidx.room.Entity

@Entity(tableName = "publishers_for_games", primaryKeys = ["publisherId", "gameId"],)
data class PublishersForGames(
    val gameId: Int,
    val publisherId: Int,
)