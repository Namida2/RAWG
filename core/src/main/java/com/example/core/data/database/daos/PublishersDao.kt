package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.core.data.games.gameDetailsResponce.Publisher

@Dao
interface PublishersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg publisher: Publisher)
}