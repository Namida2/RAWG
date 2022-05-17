package com.example.core_game.data.gameDetailsResponce

import com.google.gson.annotations.SerializedName

class Tag {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var language: String? = null
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}