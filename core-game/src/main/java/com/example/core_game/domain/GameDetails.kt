package com.example.core_game.domain

import com.example.core_game.data.gameDetailsResponce.*
import javax.inject.Inject

data class GameDetails(
    var description: String? = null,
    var released: String? = null,
    var website: String? = null,
    var ratings: ArrayList<Rating>? = null,
    var added: Int = 0,
    var playtime: Int = 0,
    var achievementsCount: Int = 0,
    var redditUrl: String? = null,
    var redditCount: Int = 0,
    var twitchCount: Int = 0,
    var youtubeCount: Int = 0,
    var reviewsTextCount: Int = 0,
    var metacriticUrl: String? = null,
    var reviewsCount: Int = 0,
    var platforms: ArrayList<PlatformDetails>? = null,
    var stores: ArrayList<Store>? = null,
    var developers: ArrayList<Developer>? = null,
    var genres: ArrayList<Genre>? = null,
    var tags: ArrayList<Tag>? = null,
    var publishers: ArrayList<Publisher>? = null,
) {

    class GameDetailsMapper @Inject constructor() :
        GameDetailsResponse.Mapper<GameDetails> {
        override fun map(gameDetailsResponse: GameDetailsResponse): GameDetails =
            GameDetails(
                gameDetailsResponse.description,
                gameDetailsResponse.released,
                gameDetailsResponse.website,
                gameDetailsResponse.ratings,
                gameDetailsResponse.added,
                gameDetailsResponse.playtime,
                gameDetailsResponse.achievementsCount,
                gameDetailsResponse.redditUrl,
                gameDetailsResponse.redditCount,
                gameDetailsResponse.twitchCount,
                gameDetailsResponse.youtubeCount,
                gameDetailsResponse.reviewsTextCount,
                gameDetailsResponse.metacriticUrl,
                gameDetailsResponse.reviewsCount,
                gameDetailsResponse.platforms,
                gameDetailsResponse.stores,
                gameDetailsResponse.developers,
                gameDetailsResponse.genres,
                gameDetailsResponse.tags,
                gameDetailsResponse.publishers
            )
    }
}
