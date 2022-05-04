package com.example.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.database.daos.CashInfoDao
import com.example.core.data.entities.FilterEntity
import com.example.core.data.database.daos.FiltersDao
import com.example.core.data.entities.CashInfo

@Database(entities = [FilterEntity::class, CashInfo::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
    abstract fun getFiltersDao(): FiltersDao
    abstract fun getCashInfoDao(): CashInfoDao
}