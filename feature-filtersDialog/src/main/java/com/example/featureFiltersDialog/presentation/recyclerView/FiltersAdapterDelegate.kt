package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFilterBinding

fun interface FilterAdapterDelegateCallback {
    fun onFilterClick(filter: Filter)
}

class FiltersAdapterDelegate(
    private val selectedBackground: Int,
    private val defaultDrawable: Int,
    private val callback: FilterAdapterDelegateCallback
) : RecyclerViewAdapterDelegate<Filter, LayoutFilterBinding>, View.OnClickListener {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is Filter

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<Filter, LayoutFilterBinding> =
        FilterViewHolder(
            defaultDrawable, selectedBackground,
            LayoutFilterBinding.inflate(inflater, container, false)
                .also { it.root.setOnClickListener(this) }
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<Filter> = diffItemCallback
    private val diffItemCallback = object : DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean =
            oldItem.categoryName + oldItem.id == newItem.categoryName + newItem.id
        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean = oldItem == newItem
    }

    override fun onClick(v: View?) {
        v?.tag?.let { callback.onFilterClick(it as Filter) }
    }
}

class FilterViewHolder(
    private val defaultDrawable: Int,
    private val selectedBackground: Int,
    private val binding: LayoutFilterBinding
) : BaseViewHolder<Filter, LayoutFilterBinding>(binding) {
    override fun onBind(item: Filter) {
        with(binding) {
            root.tag = item
            filterName.precomputeAndSetText(item.name)
            if (item.isSelected) container.setBackgroundResource(selectedBackground)
            else container.setBackgroundResource(defaultDrawable)
        }
    }
}