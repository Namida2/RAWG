package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.StoresFoeGames

@Dao
interface StoresForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg store: StoresFoeGames)
    @Query("DELETE FROM stores_for_games WHERE gameId = :gameId")
    fun delete(gameId: Int)
}