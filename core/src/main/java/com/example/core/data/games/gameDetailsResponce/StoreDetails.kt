package com.example.core.data.games.gameDetailsResponce

import com.google.gson.annotations.SerializedName

class StoreDetails {
    var id = 0
    var name: String? = null
    var slug: String? = null
    var domain: String? = null
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}