package com.example.core.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.example.core.data.entities.CashInfo

@Dao
interface CashInfoDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(cashInfo: CashInfo)
    @Query("SELECT * FROM cash_info")
    suspend fun readCashInfo(): CashInfo?
    @Query("DELETE FROM cash_info")
    suspend fun deleteAll()

    @Transaction
    suspend fun deleteAldAndSetNewCashInfo(cashInfo: CashInfo) {
        deleteAll()
        insert(cashInfo)
    }
}