package com.example.core_game.domain

import android.text.Html
import com.example.core_game.data.gameDetailsResponce.*
import com.example.core_game.data.rawGameResponse.RAWGRating
import java.util.*
import javax.inject.Inject

data class GameDetails(
    var description: String? = null,
    var released: String? = null,
    var website: String? = null,
    var added: Int = 0,
    var playtime: Int = 0,
    var achievementsCount: Int = 0,
    // Always sorted
    var ratings: List<MyRating>?,
    var redditUrl: String? = null,
    var redditCount: Int = 0,
    var twitchCount: Int = 0,
    var youtubeCount: Int = 0,
    var reviewsTextCount: Int = 0,
    var metacriticUrl: String? = null,
    var reviewsCount: Int = 0,
    var platforms: List<PlatformDetails>? = null,
    var stores: List<Store>? = null,
    var developers: List<Developer>? = null,
    var genres: List<Genre>? = null,
    var tags: List<Tag>? = null,
    var publishers: List<Publisher>? = null,
) {

    class GameDetailsMapper @Inject constructor() :
        GameDetailsResponse.Mapper<GameDetails> {
        override fun map(gameDetailsResponse: GameDetailsResponse): GameDetails =
            GameDetails(
                Html.fromHtml(
                    gameDetailsResponse.description, Html.FROM_HTML_MODE_LEGACY
                ).toString().trim(),
                gameDetailsResponse.released,
                gameDetailsResponse.website,
                gameDetailsResponse.added,
                gameDetailsResponse.playtime,
                gameDetailsResponse.achievementsCount,
                getMyRatings(gameDetailsResponse.ratings),
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

        private fun getMyRatings(rawgRatings: List<RAWGRating>?): List<MyRating> {
            val result = mutableListOf<MyRating>()
            rawgRatings?.forEach {
                it.title?.replaceFirstChar { character ->
                    if (character.isLowerCase()) character.titlecase(Locale.getDefault())
                    else character.toString()
                }?.let { title -> result.add(MyRating(title, it.count)) }
            }
            return result.sorted()
        }
    }

}
