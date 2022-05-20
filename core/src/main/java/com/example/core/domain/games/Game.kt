package com.example.core.domain.games

import android.graphics.Bitmap
import androidx.room.*
import com.example.core.data.games.entities.ScreenshotsForGame
import com.example.core.data.games.rawGameResponse.AddedByStatus
import com.example.core.data.games.rawGameResponse.RAWGame
import com.example.core.data.games.rawGameResponse.ShortScreenshot
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import javax.inject.Inject

@Entity(tableName = "game_entities")
data class GameEntity(
    @PrimaryKey
    var id: Int,
    var name: String?,
    var released: String?,
    var rating: Double = 0.0,
    var ratingTop: Int = 0,
    var metacritic: Int = 0,
    var backgroundImageUrl: String?,
    var isLiked: Boolean = false,
)

data class Game(
    @Embedded
    var gameEntity: GameEntity,
    @Relation(parentColumn = "id", entityColumn = "gameId")
    var addedByStatus: AddedByStatus? = null,
    var shortScreenshots: MutableMap<String, Bitmap?>? = null,
    @Relation(parentColumn = "id", entityColumn = "gameId")
    var shortScreenshotsUrls: List<ScreenshotsForGame>? = null,
    @Relation(parentColumn = "id", entityColumn = "gameId")
    var gameDetails: GameDetails? = null,
    var backgroundImage: Bitmap? = null
) : BaseRecyclerViewType {

    class GameMapper @Inject constructor() : RAWGame.Mapper<Game> {
        override fun map(rawGame: RAWGame): Game =
            with(rawGame) {
                Game(
                    GameEntity(id, name, released, rating, ratingTop, metacritic, backgroundImage),
                    addedByStatus.also { it?.gameId = rawGame.id },
                    getMapScreenshots(shortScreenshots),
                    shortScreenshots?.mapNotNull {
                        it.image
                        ScreenshotsForGame(rawGame.id, it.image ?: return@mapNotNull null)
                    },
                )
            }

        private fun getMapScreenshots(screenshots: List<ShortScreenshot>?): MutableMap<String, Bitmap?>? {
            val result = mutableMapOf<String, Bitmap?>()
            screenshots?.forEach { screenshot ->
                val ulr = screenshot.image ?: return@forEach
                result[ulr] = null
            }
            return result
        }
    }
}
