package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Developer

@Dao
interface DevelopersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg developer: Developer)
    @Query(
        "SELECT * FROM developers WHERE id IN " +
                "( SELECT developerId FROM developers_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Developer>
}