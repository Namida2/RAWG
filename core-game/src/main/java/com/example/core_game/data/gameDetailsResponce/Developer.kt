package com.example.core_game.data.gameDetailsResponce

import com.google.gson.annotations.SerializedName

class Developer {
    var id = 0
    var name: String? = null
    var slug: String? = null
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}