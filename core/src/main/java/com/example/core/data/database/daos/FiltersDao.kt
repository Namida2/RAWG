package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.data.entities.FilterEntity

@Dao
interface FiltersDao {
    @Query("SELECT * FROM filters")
    suspend fun getFilters(): List<FilterEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insertFilters(filters: List<FilterEntity>)

    @Query("DELETE FROM filters")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAldAndInsertNewFilters(filters: List<FilterEntity>) {
        deleteAll()
        insertFilters(filters)
    }
}