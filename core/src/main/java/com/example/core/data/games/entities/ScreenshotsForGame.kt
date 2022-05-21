package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screenshots_for_games", primaryKeys = ["screenshotUrl", "gameId"],)
data class ScreenshotsForGame(
    val gameId: Int,
    val screenshotUrl: String,
)