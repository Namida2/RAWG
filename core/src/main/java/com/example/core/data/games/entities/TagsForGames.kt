package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags_for_games")
data class TagsForGames(
    val gameId: Int,
    val storeId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)