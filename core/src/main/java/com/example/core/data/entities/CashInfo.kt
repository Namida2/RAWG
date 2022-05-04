package com.example.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cash_info")
class CashInfo(
    val lastCashedAt: Long?,
    @PrimaryKey val id: Int = 0
)