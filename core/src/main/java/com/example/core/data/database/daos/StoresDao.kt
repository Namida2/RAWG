package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Store

@Dao
interface StoresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg stores: Store)
    @Query(
        "SELECT * FROM stores WHERE id IN " +
                "( SELECT storeId FROM stores_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Store>
}