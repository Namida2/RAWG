package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.data.games.entities.ScreenshotsForGame

@Dao
interface ScreenshotsForGamesDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg screenshot: ScreenshotsForGame)
    @Query("DELETE FROM screenshots_for_games WHERE gameId = :gameId")
    fun delete(gameId: Int)
}