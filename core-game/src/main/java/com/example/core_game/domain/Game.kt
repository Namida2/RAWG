package com.example.core_game.domain

import android.graphics.Bitmap
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core_game.data.rawGameResponse.*
import javax.inject.Inject

data class Game(
    val id: Int,
    val name: String?,
    var released: String?,
    var rating: Double = 0.0,
    var ratingTop: Int = 0,
    var ratings: List<Rating>?,
    var added: Int = 0,
    var addedByStatus: AddedByStatus?,
    var metacritic: Int = 0,
    var platformsInfo: List<PlatformInfo>?,
    var genres: List<Genre>?,
    var stores: List<StoreInfo>?,
    var tags: List<Tag>?,
    var shortScreenshots: MutableMap<String, Bitmap?>?,
    var backgroundImage: Bitmap? = null,
    var isLiked: Boolean = false,
    var gameDetails: GameDetails? = null
) : BaseRecyclerViewType {
    class GameMapper @Inject constructor() : RAWGame.Mapper<Game> {
        override fun map(rawGame: RAWGame): Game =
            with(rawGame) {
                Game(
                    id, name, released, rating,
                    ratingTop, ratings, added,
                    addedByStatus, metacritic,
                    platformsInfo, genres,
                    stores, tags, kotlin.run {
                        val screenshots = mutableMapOf<String, Bitmap?>()
                        rawGame.shortScreenshots?.forEach() { screenshot ->
                            val ulr = screenshot.image ?: return@forEach
                            screenshots[ulr] = null
                        }
                        screenshots
                    }
                )
            }
    }
}
