package com.example.core.presentaton.recyclerView.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.R
import com.example.core.databinding.LayoutFilterBinding
import com.example.core.domain.entities.filters.BaseFilter
import com.example.core.domain.entities.filters.interfaces.Filter
import com.example.core.domain.entities.tools.enums.PlatformsEnum
import com.example.core.domain.entities.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.base.BaseViewHolder
import com.example.core.presentaton.recyclerView.base.RecyclerViewAdapterDelegate

fun interface FilterAdapterDelegateCallback {
    fun onFilterClick(filter: BaseFilter)
}

class FiltersAdapterDelegate(
    private val selectedBackground: Int,
    private val defaultDrawable: Int,
    private val callback: FilterAdapterDelegateCallback? = null
) : RecyclerViewAdapterDelegate<BaseFilter, LayoutFilterBinding>, View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is BaseFilter

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<BaseFilter, LayoutFilterBinding> =
        FilterViewHolder(
            defaultDrawable, selectedBackground,
            LayoutFilterBinding.inflate(inflater, container, false)
                .also { it.root.setOnClickListener(this) }
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<BaseFilter> = diffItemCallback
    private val diffItemCallback = object : DiffUtil.ItemCallback<BaseFilter>() {
        override fun areItemsTheSame(oldItem: BaseFilter, newItem: BaseFilter): Boolean =
            oldItem.categoryName + oldItem.id == newItem.categoryName + newItem.id

        override fun areContentsTheSame(oldItem: BaseFilter, newItem: BaseFilter): Boolean =
            oldItem == newItem
    }

    override fun onClick(v: View?) {
        v?.tag?.let { callback?.onFilterClick(it as BaseFilter) }
    }
}

class FilterViewHolder(
    private val defaultDrawable: Int,
    private val selectedBackground: Int,
    private val binding: LayoutFilterBinding
) : BaseViewHolder<BaseFilter, LayoutFilterBinding>(binding) {
    override fun onBind(item: BaseFilter) {
        with(binding) {
            root.tag = item
            filterName.precomputeAndSetText(item.name)
            if (item.isSelected) container.setBackgroundResource(selectedBackground)
            else container.setBackgroundResource(defaultDrawable)
            setIcon(item)
        }
    }

    private fun setIcon(filter: Filter) {
        binding.filterIcon.visibility = View.VISIBLE
        when (filter.slug) {
            PlatformsEnum.WINDOWS.slug ->         binding.filterIcon.setImageResource(PlatformsEnum.WINDOWS.iconId)
            PlatformsEnum.PLAYSTATION5.slug ->    binding.filterIcon.setImageResource(PlatformsEnum.PLAYSTATION.iconId)
            PlatformsEnum.PLAYSTATION4.slug ->    binding.filterIcon.setImageResource(PlatformsEnum.PLAYSTATION.iconId)
            PlatformsEnum.PLAYSTATION3.slug ->    binding.filterIcon.setImageResource(PlatformsEnum.PLAYSTATION.iconId)
            PlatformsEnum.PLAYSTATION2.slug ->    binding.filterIcon.setImageResource(PlatformsEnum.PLAYSTATION.iconId)
            PlatformsEnum.PLAYSTATION.slug ->     binding.filterIcon.setImageResource(PlatformsEnum.PLAYSTATION.iconId)
            PlatformsEnum.XBOX_ONE.slug ->        binding.filterIcon.setImageResource(PlatformsEnum.XBOX.iconId)
            PlatformsEnum.XBOX_360.slug ->        binding.filterIcon.setImageResource(PlatformsEnum.XBOX.iconId)
            PlatformsEnum.XBOX.slug ->            binding.filterIcon.setImageResource(PlatformsEnum.XBOX.iconId)
            PlatformsEnum.XBOX_SERIES_X.slug ->   binding.filterIcon.setImageResource(PlatformsEnum.XBOX.iconId)
            PlatformsEnum.NINTENDO_SWITCH.slug -> binding.filterIcon.setImageResource(PlatformsEnum.NINTENDO_SWITCH.iconId)
            PlatformsEnum.IOS.slug ->             binding.filterIcon.setImageResource(PlatformsEnum.IOS.iconId)
            PlatformsEnum.ANDROID.slug ->         binding.filterIcon.setImageResource(PlatformsEnum.ANDROID.iconId)
            else ->  binding.filterIcon.visibility = View.GONE
        }
    }
}