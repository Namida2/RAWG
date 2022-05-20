package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.PlatformsForGames

@Dao
interface PlatformsForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg platform: PlatformsForGames)
    @Query("DELETE FROM platforms_for_games WHERE gameId = :gameId")
    fun delete(gameId: Int)
}