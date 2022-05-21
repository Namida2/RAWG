package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.StoresFoeGames
import com.example.core.data.games.gameDetailsResponce.Platform
import com.example.core.data.games.gameDetailsResponce.Store

@Dao
interface StoresForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg store: StoresFoeGames)
    @Query("DELETE FROM stores_for_games WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)
}