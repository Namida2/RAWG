package com.example.core.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "filters")
data class FilterEntity(
    val categoryName: String,
    val name: String,
    val slug: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)