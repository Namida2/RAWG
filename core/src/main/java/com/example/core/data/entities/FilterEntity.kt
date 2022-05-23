package com.example.core.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters", primaryKeys = ["id", "category_name"])
data class FilterEntity(
    val id: Int,
    val name: String,
    val slug: String,
    @ColumnInfo(name = "category_name")
    val categoryName: String
)