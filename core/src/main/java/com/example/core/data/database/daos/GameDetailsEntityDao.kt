package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.domain.games.GameDetails
import com.example.core.domain.games.GameDetailsEntity
import com.example.core.domain.games.GameEntity

@Dao
interface GameDetailsEntityDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(gamesDetails: GameDetailsEntity)
    @Query("DELETE FROM games_details WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)
    @Query("SELECT * FROM games_details")
    suspend fun readAll(): List<GameDetails>
}