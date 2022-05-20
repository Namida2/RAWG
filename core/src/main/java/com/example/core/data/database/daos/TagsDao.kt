package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import com.example.core.data.games.gameDetailsResponce.Tag

@Dao
interface TagsDao {
    @Insert(onConflict = REPLACE)
    fun insert(vararg tag: Tag)
}