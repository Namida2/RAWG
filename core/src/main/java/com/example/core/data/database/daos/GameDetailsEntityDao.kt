package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.domain.games.GameDetailsEntity

@Dao
interface GameDetailsEntityDao {
    @Insert(onConflict = REPLACE)
    fun insert(gamesDetails: GameDetailsEntity)
    @Query("DELETE FROM games_details WHERE gameId = :gameId")
    fun delete(gameId: Int)
}