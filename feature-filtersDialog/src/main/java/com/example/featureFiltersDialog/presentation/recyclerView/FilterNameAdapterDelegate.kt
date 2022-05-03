package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFilterMameBinding
import com.example.featureFiltersDialog.domain.FilterName

class FilterNameAdapterDelegate: RecyclerViewAdapterDelegate<FilterName, LayoutFilterMameBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter_mame

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FilterName

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<FilterName, LayoutFilterMameBinding> =
        FilterNameViewHolder(
            LayoutFilterMameBinding.inflate(inflater, container, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<FilterName> = diffItemCallback

    private val diffItemCallback = object: DiffUtil.ItemCallback<FilterName>() {
        override fun areItemsTheSame(oldItem: FilterName, newItem: FilterName): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: FilterName, newItem: FilterName): Boolean = oldItem == newItem
    }
}

class FilterNameViewHolder(
    private val binding: LayoutFilterMameBinding
): BaseViewHolder<FilterName, LayoutFilterMameBinding>(binding) {
    override fun onBind(item: FilterName) {
        binding.filterName.precomputeAndSetText(item.name)
    }
}