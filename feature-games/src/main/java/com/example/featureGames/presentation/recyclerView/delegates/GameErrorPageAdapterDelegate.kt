package com.example.featureGames.presentation.recyclerView.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureGames.R
import com.example.featureGames.databinding.LayoutGameErrorPagePlaceholderBinding
import com.example.featureGames.domain.model.GameErrorPagePlaceHolder


fun interface GameErrorPageAdapterDelegateCallback {
    fun onGameErrorPagePlaceHolderClick(page: Int)
}

class GameErrorPageAdapterDelegate(
    private val callback: GameErrorPageAdapterDelegateCallback
) : RecyclerViewAdapterDelegate<GameErrorPagePlaceHolder, LayoutGameErrorPagePlaceholderBinding>,
    View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.layout_game_error_page_placeholder

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is GameErrorPagePlaceHolder

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<GameErrorPagePlaceHolder, LayoutGameErrorPagePlaceholderBinding> {
        val binding =
            LayoutGameErrorPagePlaceholderBinding.inflate(inflater, container, false).also {
                it.root.setOnClickListener(this)
            }
        binding.root.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val layoutParams = binding.root.layoutParams
                if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                    val layoutManagerParams: StaggeredGridLayoutManager.LayoutParams =
                        layoutParams.also { it.isFullSpan = true }
                    binding.root.layoutParams = layoutManagerParams
                    ((container as RecyclerView).layoutManager as StaggeredGridLayoutManager)
                        .invalidateSpanAssignments()
                }
                binding.root.viewTreeObserver.removeOnPreDrawListener(this)
                return true
            }
        })
        return GameErrorPageViewHolder(binding)
    }

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<GameErrorPagePlaceHolder> =
        diffUtilItemCallback

    private val diffUtilItemCallback = object : DiffUtil.ItemCallback<GameErrorPagePlaceHolder>() {
        override fun areItemsTheSame(
            oldItem: GameErrorPagePlaceHolder,
            newItem: GameErrorPagePlaceHolder
        ): Boolean = oldItem.page == newItem.page

        override fun areContentsTheSame(
            oldItem: GameErrorPagePlaceHolder,
            newItem: GameErrorPagePlaceHolder
        ): Boolean = oldItem.page == newItem.page

    }

    override fun onClick(v: View?) {
        v?.tag?.let { callback.onGameErrorPagePlaceHolderClick(it as Int) }
    }
}

class GameErrorPageViewHolder(
    private val binding: LayoutGameErrorPagePlaceholderBinding
) : BaseViewHolder<GameErrorPagePlaceHolder, LayoutGameErrorPagePlaceholderBinding>(binding) {
    override fun onBind(item: GameErrorPagePlaceHolder) {
        binding.root.tag = item.page
    }
}