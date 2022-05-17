package com.example.core_game.data.gameDetailsResponce

import com.google.gson.annotations.SerializedName

class Genre {
    var id = 0
    var name: String? = null
    var slug: String? = null
    @SerializedName("gamesCount")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}