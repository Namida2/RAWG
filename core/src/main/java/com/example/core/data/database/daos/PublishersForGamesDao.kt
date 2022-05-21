package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.PublishersForGames

@Dao
interface PublishersForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg publisher: PublishersForGames)
    @Query("DELETE FROM publishers_for_games WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)
}