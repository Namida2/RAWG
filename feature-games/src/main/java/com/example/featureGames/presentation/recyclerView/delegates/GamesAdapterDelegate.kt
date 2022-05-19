package com.example.featureGames.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.domain.tools.extensions.startDefaultRecyclerViewItemAnimation
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.base.BaseViewHolder
import com.example.core.presentaton.recyclerView.base.RecyclerViewAdapterDelegate
import com.example.core_game.domain.Game
import com.example.featureGames.R
import com.example.featureGames.databinding.LayoutGameBinding

interface GamesAdapterDelegateCallback {
    fun onGameClick(clickedGameInfo: ClickedGameInfo)
    fun onGameLikeButtonClick(game: Game)
}

data class ClickedGameInfo(val gameRootView: View, val gameId: Int)

class GamesAdapterDelegate(
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
            LayoutGameBinding.inflate(inflater, container, false).also {
                it.root.setOnClickListener { view ->
                    view?.tag?.let { tag -> callback.onGameClick(tag as ClickedGameInfo) }
                }
                it.gamePreviewImage.setOnClickListener { view ->
                    view?.tag?.let { tag -> callback.onGameClick(tag as ClickedGameInfo) }
                }
                it.likeButtonContainer.setOnClickListener { view ->
                    view?.tag?.let { tag -> callback.onGameLikeButtonClick(tag as Game) }
                }
            }
        )
    }

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<Game> = diffItemCallback
    private val diffItemCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem.id == oldItem.id

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem == oldItem
    }
}

class GamesViewGolder(
    private val binding: LayoutGameBinding
) : BaseViewHolder<Game, LayoutGameBinding>(binding) {
    override fun onBind(item: Game) {
        with(binding) {
            val tag = ClickedGameInfo(root, item.id)
            root.tag = tag
            gamePreviewImage.tag = tag
            root.transitionName = item.id.toString()
            gameName.precomputeAndSetText(item.name)
            addedCount.precomputeAndSetText(item.added.toString())
            metacriticRating.precomputeAndSetText(item.metacritic.toString())
            gamePreviewImage.setImageBitmap(item.backgroundImage)
        }
        if(item.backgroundImage == null)
            binding.root.startDefaultRecyclerViewItemAnimation()
    }
}

