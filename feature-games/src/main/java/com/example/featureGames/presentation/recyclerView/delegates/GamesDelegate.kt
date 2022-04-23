package com.example.featureGames.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.BaseRecyclerViewDelegate
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.featureGames.R
import com.example.featureGames.databinding.LayoutGameBinding
import com.example.featureGames.domain.model.Game

class GamesDelegate : BaseRecyclerViewDelegate<Game, LayoutGameBinding> {
    override val layoutId: Int
        get() = R.layout.layout_game

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is Game

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<Game, LayoutGameBinding> {
        return GamesViewGolder(
            LayoutGameBinding.inflate(inflater, parent, false)
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
            gameName.precomputeAndSetText(item.name)
            addedCount.precomputeAndSetText(item.added.toString())
            metacriticRating.precomputeAndSetText(item.metacritic.toString())
            gamePreviewImage.setImageBitmap(item.backgroundImage)
        }
    }
}

