package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.domain.games.Game
import com.example.core.domain.games.GameEntity

@Dao
interface GameEntitiesDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(gameEntity: GameEntity)
    @Query("DELETE FROM game_entities WHERE id = :gameId")
    suspend fun delete(gameId: Int)

    @Query("SELECT * FROM game_entities")
    suspend fun readAll(): List<Game>?
}