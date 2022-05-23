package com.example.core.data.games.gameDetailsResponce

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.core.domain.entities.filters.interfaces.Filter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tags")
class Tag: Filter {
    @PrimaryKey
    override var id = 0
    override var name: String? = null
    override var slug: String? = null
    @Ignore
    var language: String? = null
    @Ignore
    @SerializedName("games_count")
    var gamesCount = 0
    @Ignore
    @SerializedName("image_background")
    var imageBackground: String? = null
}