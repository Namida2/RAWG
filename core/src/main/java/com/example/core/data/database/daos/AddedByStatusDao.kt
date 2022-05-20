package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.data.games.rawGameResponse.AddedByStatus

@Dao
interface AddedByStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(addedByStatus: AddedByStatus)
    @Query("DELETE FROM added_by_status WHERE gameId = :gameId")
    fun delete(gameId: Int)
}