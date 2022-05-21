package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Genre
import com.example.core.data.games.gameDetailsResponce.Tag

@Dao
interface TagsDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg tag: Tag)
    @Query(
        "SELECT * FROM tags WHERE id IN " +
                "( SELECT tagId FROM tags_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Tag>
}