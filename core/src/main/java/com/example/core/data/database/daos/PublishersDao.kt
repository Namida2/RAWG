package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Genre
import com.example.core.data.games.gameDetailsResponce.Publisher

@Dao
interface PublishersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg publisher: Publisher)
    @Query(
        "SELECT * FROM publishers WHERE id IN " +
                "( SELECT publisherId FROM publishers_for_games WHERE gameId = :gameId)"
    )
    suspend fun readByGameId(gameId: Int): List<Publisher>
}