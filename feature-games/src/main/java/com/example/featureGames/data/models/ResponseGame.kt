package com.example.featureGames.data.models

import com.google.gson.annotations.SerializedName
import java.util.*

class ResponseGame {
    var id = 0
    var slug: String? = null
    var name: String? = null
    var released: String? = null
    var tba = false

    @SerializedName("background_image")
    var backgroundImage: String? = null
    var rating = 0.0

    @SerializedName("rating_top")
    var ratingTop = 0
    var ratings: ArrayList<Rating>? = null

    @SerializedName("ratings_count")
    var ratingsCount = 0

    @SerializedName("reviews_text_count")
    var reviewsTextCount = 0
    var added = 0

    @SerializedName("added_by_status")
    var addedByStatus: AddedByStatus? = null
    var metacritic = 0
    var playtime = 0

    @SerializedName("suggestions_count")
    var suggestionsCount = 0
    var updated: Date? = null

    @SerializedName("user_game")
    var userGame: Any? = null

    @SerializedName("reviews_count")
    var reviewsCount = 0

    @SerializedName("saturated_color")
    var saturatedColor: String? = null

    @SerializedName("dominant_color")
    var dominantColor: String? = null

    @SerializedName("platforms")
    var platformsInfo: ArrayList<PlatformInfo>? = null

    @SerializedName("parent_platforms")
    var parentPlatforms: ArrayList<ParentPlatform>? = null
    var genres: ArrayList<Genre>? = null
    var stores: ArrayList<StoreInfo>? = null
    var clip: Any? = null
    var tags: ArrayList<Tag>? = null

    @SerializedName("esrb_rating")
    var esrbRating: EsrbRating? = null

    @SerializedName("short_screenshots")
    var shortScreenshots: ArrayList<ShortScreenshot>? = null
}