package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.core.data.games.gameDetailsResponce.Genre

@Dao
interface GenresDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg genre: Genre)
}