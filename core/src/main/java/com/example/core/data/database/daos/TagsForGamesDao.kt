package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.entities.TagsForGames

@Dao
interface TagsForGamesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg tag: TagsForGames)
    @Query("DELETE FROM tags_for_games WHERE gameId = :gameId")
    fun delete(gameId: Int)
}