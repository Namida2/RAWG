package com.example.core.domain.games

import android.text.Html
import androidx.room.*
import com.example.core.data.games.gameDetailsResponce.*
import com.example.core.data.games.rawGameResponse.RAWGRating
import java.util.*
import javax.inject.Inject

@Entity(tableName = "games_details")
data class GameDetailsEntity(
    @PrimaryKey
    var gameId: Int,
    var description: String? = null,
    var released: String? = null,
    var website: String? = null,
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
)

data class GameDetails(
    @Embedded
    val gameDetailsEntity: GameDetailsEntity,
    // Always sorted
    @Relation(parentColumn = "gameId", entityColumn = "gameId")
    var ratings: List<MyRating>? = null,
    var platforms: List<Platform>? = null,
    var stores: List<Store>? = null,
    var developers: List<Developer>? = null,
    var genres: List<Genre>? = null,
    var tags: List<Tag>? = null,
    var publishers: List<Publisher>? = null,
) {
    class GameDetailsMapper @Inject constructor() :
        GameDetailsResponse.Mapper<GameDetails> {
        override fun map(gameDetailsResponse: GameDetailsResponse): GameDetails =
            with(gameDetailsResponse) {
                GameDetails(
                    GameDetailsEntity(
                        id, Html.fromHtml(
                            description, Html.FROM_HTML_MODE_LEGACY
                        ).toString().trim(), released,
                        website, added,
                        playtime, achievementsCount,
                        redditUrl, redditCount,
                        twitchCount, youtubeCount
                    ),
                    getMyRatings(id, ratings),
                    platforms?.mapNotNull { platformInfo ->
                        platformInfo.requirements?.minimum?.let {
                            platformInfo.platform?.requirementsMinimum = Html.fromHtml(
                                it, Html.FROM_HTML_MODE_LEGACY
                            ).toString().trim()
                        }
                        platformInfo.requirements?.recommended?.let {
                            platformInfo.platform?.requirementsRecommended = Html.fromHtml(
                                it, Html.FROM_HTML_MODE_LEGACY
                            ).toString().trim()
                        }
                        platformInfo.platform
                    },
                    stores?.onEach {
                        it.store?.let { store ->
                            it.id  = store.id
                            it.name = store.name
                            it.slug = store.slug
                        }
                    },
                    developers,
                    genres,
                    tags,
                    publishers
                )
            }

        private fun getMyRatings(gameId: Int, rawgRatings: List<RAWGRating>?): List<MyRating> {
            val result = mutableListOf<MyRating>()
            rawgRatings?.forEach {
                it.title?.replaceFirstChar { character ->
                    if (character.isLowerCase()) character.titlecase(Locale.getDefault())
                    else character.toString()
                }?.let { title -> result.add(MyRating(gameId, title, it.count)) }
            }
            return result.sorted()
        }
    }

}
