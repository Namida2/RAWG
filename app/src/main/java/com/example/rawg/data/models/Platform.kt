package com.example.rawg.data.models

import com.google.gson.annotations.SerializedName

class Platform {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var image: Any? = null
    @SerializedName("year_end")
    var yearEnd: Any? = null
    @SerializedName("year_start")
    var yearStart = 0
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}