package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.GenresForGames
import com.example.core.data.games.gameDetailsResponce.Developer
import com.example.core.data.games.gameDetailsResponce.Genre

@Dao
interface GenresForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg genre: GenresForGames)
    @Query("DELETE FROM genres_for_games WHERE gameId = :gameId")
    suspend fun delete(gameId: Int)
}