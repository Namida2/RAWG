package com.example.featureGameDetails.presentation.viewPager.adapterDelegates

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.tools.extensions.prepareScaleAnimation
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.base.BaseViewHolder
import com.example.core.presentaton.recyclerView.base.RecyclerViewAdapterDelegate
import com.example.featureGameDetails.R
import com.example.featureGameDetails.databinding.LayoutGameScreenshotBinding
import com.example.featureGameDetails.domain.entities.GameScreenshot

class GameScreenshotAdapterDelegate :
    RecyclerViewAdapterDelegate<GameScreenshot, LayoutGameScreenshotBinding> {
    override val layoutId: Int
        get() = R.layout.layout_game_screenshot

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is GameScreenshot

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<GameScreenshot, LayoutGameScreenshotBinding> =
        GameScreenshotViewHolder(
            LayoutGameScreenshotBinding.inflate(inflater, container, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<GameScreenshot> = diffItemCallback

    private val diffItemCallback = object : DiffUtil.ItemCallback<GameScreenshot>() {
        override fun areItemsTheSame(oldItem: GameScreenshot, newItem: GameScreenshot): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: GameScreenshot, newItem: GameScreenshot): Boolean =
            oldItem == newItem
    }
}

class GameScreenshotViewHolder(
    private val binding: LayoutGameScreenshotBinding
) : BaseViewHolder<GameScreenshot, LayoutGameScreenshotBinding>(binding) {
    override fun onBind(item: GameScreenshot) {
        binding.screenShotImageView.setImageBitmap(item.screenshot)
        binding.root.prepareScaleAnimation(interpolator = OvershootInterpolator()).start()
    }
}