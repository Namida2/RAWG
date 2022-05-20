package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.data.games.entities.DevelopersForGames

@Dao
interface DevelopersForGamesDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg developer: DevelopersForGames)
    @Query("DELETE FROM developers_for_games WHERE gameId = :gameId")
    fun delete(gameId: Int)
}