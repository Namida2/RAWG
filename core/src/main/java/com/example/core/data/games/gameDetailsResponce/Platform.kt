package com.example.core.data.games.gameDetailsResponce

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "platforms")
class Platform {
    @PrimaryKey
    var id = 0
    var name: String? = null
    var slug: String? = null
    var requirementsMinimum: String? = null
    var requirementsRecommended: String? = null
    @Ignore
    var image: Any? = null
    @Ignore
    @SerializedName("year_end")
    var yearEnd: Any? = null
    @Ignore
    @SerializedName("year_start")
    var yearStart: Any? = null
    @Ignore
    @SerializedName("games_count")
    var gamesCount = 0
    @Ignore
    @SerializedName("image_background")
    var imageBackground: String? = null
}