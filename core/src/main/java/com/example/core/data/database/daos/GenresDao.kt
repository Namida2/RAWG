package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Genre

@Dao
interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg genre: Genre)
    @Query(
        "SELECT * FROM genres WHERE id IN " +
                "( SELECT genreId FROM genres_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Genre>
}