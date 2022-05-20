package com.example.core.data.games.gameDetailsResponce

import com.example.core.data.games.rawGameResponse.AddedByStatus
import com.example.core.data.games.rawGameResponse.RAWGRating
import com.google.gson.annotations.SerializedName

class GameDetailsResponse {
    var id = 0
    var slug: String? = null
    var name: String? = null

    @SerializedName("name_original")
    var nameOriginal: String? = null
    var description: String? = null
    var metacritic: Any? = null

    @SerializedName("metacritic_platforms")
    var metacriticPlatforms: ArrayList<Any>? = null
    var released: String? = null
    var tba = false
    var updated: String? = null

    @SerializedName("background_image")
    var backgroundImage: String? = null

    @SerializedName("background_image_additional")
    var backgroundImageAdditional: String? = null
    var website: String? = null
    var rating = 0.0

    @SerializedName("rating_top")
    var ratingTop = 0
    var ratings: ArrayList<RAWGRating>? = null
    var reactions: Any? = null
    var added = 0

    @SerializedName("added_by_status")
    var addedByStatus: AddedByStatus? = null
    var playtime = 0

    @SerializedName("screenshots_count")
    var screenshotsCount = 0

    @SerializedName("movies_count")
    var moviesCount = 0

    @SerializedName("creators_count")
    var creatorsCount = 0

    @SerializedName("achievements_count")
    var achievementsCount = 0

    @SerializedName("parent_achievements_count")
    var parentAchievementsCount = 0

    @SerializedName("reddit_url")
    var redditUrl: String? = null

    @SerializedName("reddit_name")
    var redditName: String? = null

    @SerializedName("reddit_description")
    var redditDescription: String? = null

    @SerializedName("reddit_logo")
    var redditLogo: String? = null

    @SerializedName("reddit_count")
    var redditCount = 0

    @SerializedName("twitch_count")
    var twitchCount = 0

    @SerializedName("youtube_count")
    var youtubeCount = 0

    @SerializedName("reviews_text_count")
    var reviewsTextCount = 0

    @SerializedName("ratings_count")
    var ratingsCount = 0

    @SerializedName("suggestions_count")
    var suggestionsCount = 0

    @SerializedName("alternative_names")
    var alternativeNames: ArrayList<Any>? = null

    @SerializedName("metacritic_url")
    var metacriticUrl: String? = null

    @SerializedName("parents_count")
    var parentsCount = 0

    @SerializedName("additions_count")
    var additionsCount = 0

    @SerializedName("game_series_count")
    var gameSeriesCount = 0

    @SerializedName("user_game")
    var userGame: Any? = null

    @SerializedName("reviews_count")
    var reviewsCount = 0

    @SerializedName("saturated_color")
    var saturatedColor: String? = null

    @SerializedName("dominant_color")
    var dominantColor: String? = null

    @SerializedName("parent_platforms")
    var parentPlatforms: ArrayList<ParentPlatform>? = null
    var platforms: ArrayList<PlatformDetails>? = null
    var stores: ArrayList<Store>? = null
    var developers: ArrayList<Developer>? = null
    var genres: ArrayList<Genre>? = null
    var tags: ArrayList<Tag>? = null
    var publishers: ArrayList<Publisher>? = null

    @SerializedName("esrb_rating")
    var esrbRating: EsrbRating? = null
    var clip: Any? = null

    @SerializedName("description_raw")
    var descriptionRaw: String? = null

    interface Mapper<T> {
        fun map(gameDetailsResponse: GameDetailsResponse): T
    }
}
