package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Platform

@Dao
interface PlatformsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg platform: Platform)

    @Query(
        "SELECT * FROM platforms WHERE id IN " +
                "( SELECT platformId FROM platforms_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Platform>
}