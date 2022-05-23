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
    var gameDetailsEntity: GameDetailsEntity,
    // Always sorted
    @Relation(parentColumn = "gameId", entityColumn = "gameId")
    var ratings: List<MyRating>? = null,
    @Ignore
    var stores: List<Store>? = null,
    @Ignore
    var developers: List<Developer>? = null,
    @Ignore
    var genres: List<Genre>? = null,
    @Ignore
    var tags: List<Tag>? = null,
    @Ignore
    var publishers: List<Publisher>? = null,
) {
    constructor(): this(GameDetailsEntity(0))
    class GameDetailsMapper @Inject constructor() :
        GameDetailsResponse.Mapper<GameDetails> {
        override fun map(gameDetailsResponse: GameDetailsResponse): GameDetails =
            with(gameDetailsResponse) {
                GameDetails(
                    GameDetailsEntity(
                        id, Html.fromHtml(
                            description, Html.FROM_HTML_MODE_LEGACY
                        ).toString().trim(), released,
                        website, playtime, achievementsCount,
                        redditUrl, redditCount,
                        twitchCount, youtubeCount
                    ),
                    getMyRatings(id, ratings),
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
