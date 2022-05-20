package com.example.core.data.games.gameDetailsResponce

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "genres")
class Genre {
    @PrimaryKey
    var id = 0
    var name: String? = null
    var slug: String? = null
    @Ignore
    @SerializedName("games_count")
    var gamesCount = 0
    @Ignore
    @SerializedName("image_background")
    var imageBackground: String? = null
}