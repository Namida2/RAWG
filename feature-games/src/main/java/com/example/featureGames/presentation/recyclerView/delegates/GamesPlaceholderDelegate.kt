package com.example.featureGames.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.tools.extensions.prepareDefaultSpringAnimation
import com.example.core.domain.tools.extensions.startDefaultRecyclerViewItemAnimation
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.featureGames.R
import com.example.featureGames.databinding.LayoutGamePlaceholderBinding
import com.example.featureGames.domain.entities.GamePlaceHolder

class GamesPlaceholderDelegate: RecyclerViewAdapterDelegate<GamePlaceHolder, LayoutGamePlaceholderBinding> {
    override val layoutId: Int
        get() = R.layout.layout_game_placeholder

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is GamePlaceHolder

    override fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<GamePlaceHolder, LayoutGamePlaceholderBinding> {
        return GamesPlaceHolderViewGolder(
            LayoutGamePlaceholderBinding.inflate(inflater, parent, false)
        )
    }
    override fun getDiffItemCallback(): DiffUtil.ItemCallback<GamePlaceHolder> = diffItemCallback
    private val diffItemCallback = object: DiffUtil.ItemCallback<GamePlaceHolder>() {
        override fun areItemsTheSame(oldItem: GamePlaceHolder, newItem: GamePlaceHolder): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: GamePlaceHolder, newItem: GamePlaceHolder): Boolean = oldItem.id == newItem.id
    }
}

class GamesPlaceHolderViewGolder(
    private val binding: LayoutGamePlaceholderBinding
): BaseViewHolder<GamePlaceHolder, LayoutGamePlaceholderBinding>(binding) {
    override fun onBind(item: GamePlaceHolder) {
        binding.root.startDefaultRecyclerViewItemAnimation()
    }
}