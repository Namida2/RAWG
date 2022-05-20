package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "developers_for_games")
data class DevelopersForGames(
    val gameId: Int,
    val developerId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)