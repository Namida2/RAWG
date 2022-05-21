package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.data.games.entities.DevelopersForGames
import com.example.core.data.games.gameDetailsResponce.Developer
import com.example.core.data.games.gameDetailsResponce.Store

@Dao
interface DevelopersForGamesDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg developer: DevelopersForGames)
    @Query("DELETE FROM developers_for_games WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)

}