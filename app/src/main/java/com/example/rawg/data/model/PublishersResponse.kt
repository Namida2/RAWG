package com.example.rawg.data.model

import com.example.rawg.data.model.interfaces.FilterParams
import com.google.gson.annotations.SerializedName

class PublishersResult: FilterParams {
    var id = 0
    override var name: String? = null
    override var slug: String? = null
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
    var games: ArrayList<Game>? = null
}
