package com.example.featureGames.presentation.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.featureGames.databinding.LayoutGameCardBinding
import com.example.featureGames.R
import com.example.core.presentaton.recyclerView.BaseRecyclerViewDelegate
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.featureGames.domain.model.Game

class GamesDelegate: BaseRecyclerViewDelegate<Game, LayoutGameCardBinding> {
    override val layoutId: Int
        get() = R.layout.layout_game_card

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is Game

    override fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<Game, LayoutGameCardBinding> {
        return GamesViewGolder(
            LayoutGameCardBinding.inflate(inflater, parent, false)
        )
    }
    override fun getDiffItemCallback(): DiffUtil.ItemCallback<Game> = diffItemCallback
    private val diffItemCallback = object: DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem.id == oldItem.id
        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean =
            newItem.id == oldItem.id
    }
}

class GamesViewGolder(
    private val binding: LayoutGameCardBinding
): BaseViewHolder<Game, LayoutGameCardBinding>(binding) {

    override fun onBind(item: Game) {

    }

}

