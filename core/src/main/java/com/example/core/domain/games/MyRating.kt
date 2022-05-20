package com.example.core.domain.games

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_ratings")
data class MyRating(
    val gameId: Int,
    val title: String,
    val count: Int,
    @PrimaryKey
    val id: Int = 0
) : Comparable<MyRating> {
    override fun compareTo(other: MyRating): Int = other.count - this.count
}