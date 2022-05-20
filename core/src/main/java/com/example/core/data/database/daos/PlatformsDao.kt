package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.gameDetailsResponce.Platform
import com.example.core.domain.games.MyRating

@Dao
interface PlatformsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg platform: Platform)
}