package com.example.core.data.games.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "screenshots_for_games")
data class ScreenshotsForGame(
    val gameId: Int,
    val screenshotUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)