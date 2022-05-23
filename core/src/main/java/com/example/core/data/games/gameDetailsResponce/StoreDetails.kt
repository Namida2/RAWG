package com.example.core.data.games.gameDetailsResponce

import com.example.core.domain.entities.filters.interfaces.Filter
import com.google.gson.annotations.SerializedName

class StoreDetails: Filter {
    override var id = 0
    override var name: String? = null
    override var slug: String? = null
    var domain: String? = null
    @SerializedName("games_count")
    var gamesCount = 0
    @SerializedName("image_background")
    var imageBackground: String? = null
}