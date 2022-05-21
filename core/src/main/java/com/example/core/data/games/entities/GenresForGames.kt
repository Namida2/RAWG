package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "genres_for_games", primaryKeys = ["genreId", "gameId"])
data class GenresForGames(
    val gameId: Int,
    val genreId: Int,
)