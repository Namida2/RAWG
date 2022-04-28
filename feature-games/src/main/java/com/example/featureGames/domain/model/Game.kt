package com.example.featureGames.domain.model

import android.graphics.Bitmap
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureGames.data.entities.rawGameResponse.*
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
    var shortScreenshots: List<Bitmap>? = null,
    var backgroundImage: Bitmap? = null,
    var isLiked: Boolean = false
) : BaseRecyclerViewType {
    class GameMapper @Inject constructor() : RAWGame.Mapper<Game> {
        override fun map(RAW: RAWGame): Game =
            with(RAW) {
                Game(
                    id, name, released, rating,
                    ratingTop, ratings, added,
                    addedByStatus, metacritic,
                    platformsInfo, genres,
                    stores, tags
                )
            }
    }
}
