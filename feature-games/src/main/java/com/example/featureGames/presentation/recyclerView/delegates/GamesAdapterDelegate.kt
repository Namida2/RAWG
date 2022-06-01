package com.example.featureGames.presentation.recyclerView.delegates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.enums.PlatformsEnum
import com.example.core.domain.games.Game
import com.example.core.domain.entities.tools.extensions.precomputeAndSetText
import com.example.core.domain.entities.tools.extensions.setIconColorFilter
import com.example.core.domain.entities.tools.extensions.startDefaultRecyclerViewItemAnimation
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.base.BaseViewHolder
import com.example.core.presentaton.recyclerView.base.RecyclerViewAdapterDelegate
import com.example.featureGames.R
import com.example.featureGames.databinding.LayoutGameBinding

interface GamesAdapterDelegateCallback {
    fun onGameClick(clickedGameInfo: ClickedGameInfo)
    fun onGameLikeButtonClick(game: Game)
}

data class ClickedGameInfo(val gameRootView: View, val gameId: Int)

class GamesAdapterDelegate(
    // screenTag for transition name for material containerTransform
    private val screenTag: GameScreenTags,
    private val callback: GamesAdapterDelegateCallback
) : RecyclerViewAdapterDelegate<Game, LayoutGameBinding> {
    override val layoutId: Int
        get() = R.layout.layout_game

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is Game

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<Game, LayoutGameBinding> {
        return GamesViewGolder(
            screenTag,
            LayoutGameBinding.inflate(inflater, container, false).also {
                it.root.setOnClickListener { view ->
                    view?.tag?.let { tag -> callback.onGameClick(tag as ClickedGameInfo) }
                }
                it.gamePreviewImage.setOnClickListener { view ->
                    view?.tag?.let { tag -> callback.onGameClick(tag as ClickedGameInfo) }
                }
                it.likeButtonContainer.setOnClickListener { view ->
                    view?.tag?.let { tag ->
                        callback.onGameLikeButtonClick((tag as Game))
                    }
                }
            }
        )
    }

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<Game> = diffItemCallback
    private val diffItemCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem.gameEntity.id == oldItem.gameEntity.id

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem == oldItem
    }
}

class GamesViewGolder(
    private val screenTag: GameScreenTags,
    private val binding: LayoutGameBinding
) : BaseViewHolder<Game, LayoutGameBinding>(binding) {
    override fun onBind(item: Game) {
        with(binding) {
            val tag = ClickedGameInfo(root, item.gameEntity.id)
            root.tag = tag
            gamePreviewImage.tag = tag
            likeButtonContainer.tag = item
            root.transitionName = item.gameEntity.id.toString() + screenTag
            gameName.precomputeAndSetText(item.gameEntity.name)
            addedCount.precomputeAndSetText(item.gameEntity.added.toString())
            metacriticRating.precomputeAndSetText(item.gameEntity.metacritic.toString())
            gamePreviewImage.setImageBitmap(item.backgroundImage)
            if (item.gameEntity.isLiked) likeIcon.setIconColorFilter(root.context,  com.example.core.R.color.rad)
            else likeIcon.setIconColorFilter(root.context, com.example.core.R.color.grayLite)
            setIconVisibility(item)
        }
        binding.root.startDefaultRecyclerViewItemAnimation()
    }

    private fun setIconVisibility(game: Game) {
        with(binding) {
            icPc.visibility = View.GONE
            icPlaystation.visibility = View.GONE
            icPlaystation.visibility = View.GONE
            icPlaystation.visibility = View.GONE
            icPlaystation.visibility = View.GONE
            icPlaystation.visibility = View.GONE
            icXbox.visibility = View.GONE
            icXbox.visibility = View.GONE
            icXbox.visibility = View.GONE
            icXbox.visibility = View.GONE
            icNintendoSwitch.visibility = View.GONE
            icIOS.visibility = View.GONE
            icIOS.visibility = View.GONE
            game.platforms?.forEach {
                when(it.slug) {
                    PlatformsEnum.WINDOWS.slug ->         icPc.visibility = View.VISIBLE
                    PlatformsEnum.PLAYSTATION5.slug ->    icPlaystation.visibility = View.VISIBLE
                    PlatformsEnum.PLAYSTATION4.slug ->    icPlaystation.visibility = View.VISIBLE
                    PlatformsEnum.PLAYSTATION3.slug ->    icPlaystation.visibility = View.VISIBLE
                    PlatformsEnum.PLAYSTATION2.slug ->    icPlaystation.visibility = View.VISIBLE
                    PlatformsEnum.PLAYSTATION.slug ->     icPlaystation.visibility = View.VISIBLE
                    PlatformsEnum.XBOX_ONE.slug ->        icXbox.visibility = View.VISIBLE
                    PlatformsEnum.XBOX_360.slug ->        icXbox.visibility = View.VISIBLE
                    PlatformsEnum.XBOX.slug ->            icXbox.visibility = View.VISIBLE
                    PlatformsEnum.XBOX_SERIES_X.slug ->   icXbox.visibility = View.VISIBLE
                    PlatformsEnum.NINTENDO_SWITCH.slug -> icNintendoSwitch.visibility = View.VISIBLE
                    PlatformsEnum.IOS.slug ->             icIOS.visibility = View.VISIBLE
                    PlatformsEnum.ANDROID.slug ->         icIOS.visibility = View.VISIBLE
                }
            }
        }
    }

}

