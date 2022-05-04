package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFilterBinding
import com.example.core.domain.entities.filters.Filter

class FiltersAdapterDelegate: RecyclerViewAdapterDelegate<Filter, LayoutFilterBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is Filter

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<Filter, LayoutFilterBinding> =
        FilterViewHolder(
            LayoutFilterBinding.inflate(inflater, container, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<Filter> = diffItemCallback
    private val diffItemCallback = object: DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean = oldItem.name == newItem.name

    }
}

class FilterViewHolder(
    private val binding: LayoutFilterBinding
): BaseViewHolder<Filter, LayoutFilterBinding>(binding) {
    override fun onBind(item: Filter) {
       binding.filterName.precomputeAndSetText(item.name)
    }
}