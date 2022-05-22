package com.example.featureGames.presentation.recyclerView.delegates

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.tools.enums.GameScreenTags
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
            addedCount.precomputeAndSetText(item.gameDetails?.gameDetailsEntity?.added.toString())
            metacriticRating.precomputeAndSetText(item.gameEntity.metacritic.toString())
            gamePreviewImage.setImageBitmap(item.backgroundImage)
            if (item.gameEntity.isLiked) likeIcon.setIconColorFilter(root.context,  com.example.core.R.color.rad)
            else likeIcon.setIconColorFilter(root.context, com.example.core.R.color.grayLite)
        }
        binding.root.startDefaultRecyclerViewItemAnimation()
    }

}

