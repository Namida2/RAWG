package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.domain.games.MyRating

@Dao
interface MyRatingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg myRating: MyRating)
    @Query("DELETE FROM my_ratings WHERE gameId = :gameId")
    fun delete(gameId: Int)
}