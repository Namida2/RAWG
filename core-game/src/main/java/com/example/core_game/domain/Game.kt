package com.example.core_game.domain

import android.graphics.Bitmap
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core_game.data.rawGameResponse.*
import java.util.*
import javax.inject.Inject

data class Game(
    val id: Int,
    val name: String?,
    var released: String?,
    var rating: Double = 0.0,
    var ratingTop: Int = 0,
    var added: Int = 0,
    var addedByStatus: AddedByStatus?,
    var metacritic: Int = 0,
    var shortScreenshots: MutableMap<String, Bitmap?>?,
    var backgroundImage: Bitmap? = null,
    var isLiked: Boolean = false,
    var gameDetails: GameDetails? = null
) : BaseRecyclerViewType {
    class GameMapper @Inject constructor() : RAWGame.Mapper<Game> {
        override fun map(rawGame: RAWGame): Game =
            with(rawGame) {
                Game(
                    id, name, released, rating, ratingTop,
                    added, addedByStatus, metacritic,
                    getMapScreenshots(shortScreenshots)
                )
            }

        private fun getMapScreenshots(screenshots: List<ShortScreenshot>?): MutableMap<String, Bitmap?>? {
            val result = mutableMapOf<String, Bitmap?>()
            screenshots?.forEach() { screenshot ->
                val ulr = screenshot.image ?: return@forEach
                result[ulr] = null
            }
            return result
        }
    }
}
