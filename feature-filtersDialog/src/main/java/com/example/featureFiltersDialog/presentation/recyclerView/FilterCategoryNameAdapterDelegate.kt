package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.core.domain.entities.filters.FilterCategoryName
import com.example.core.domain.tools.extensions.precomputeAndSetText
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.base.BaseViewHolder
import com.example.core.presentaton.recyclerView.base.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFilterNameBinding

class FilterCategoryNameAdapterDelegate :
    RecyclerViewAdapterDelegate<FilterCategoryName, LayoutFilterNameBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter_name

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FilterCategoryName

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<FilterCategoryName, LayoutFilterNameBinding> =
        FilterCategoryNameViewHolder(
            LayoutFilterNameBinding.inflate(inflater, container, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<FilterCategoryName> = diffItemCallback

    private val diffItemCallback = object : DiffUtil.ItemCallback<FilterCategoryName>() {
        override fun areItemsTheSame(
            oldItem: FilterCategoryName,
            newItem: FilterCategoryName
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: FilterCategoryName,
            newItem: FilterCategoryName
        ): Boolean = oldItem == newItem
    }
}

class FilterCategoryNameViewHolder(
    private val binding: LayoutFilterNameBinding
) : BaseViewHolder<FilterCategoryName, LayoutFilterNameBinding>(binding) {
    private val colon = ":"
    override fun onBind(item: FilterCategoryName) {
        binding.filterName.precomputeAndSetText(item.name + colon)
    }
}