package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags_for_games", primaryKeys = ["tagId", "gameId"])
data class TagsForGames(
    val gameId: Int,
    val tagId: Int,
)