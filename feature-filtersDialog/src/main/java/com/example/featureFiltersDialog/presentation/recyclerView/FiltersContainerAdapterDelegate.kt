package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFiltersContainerBinding
import com.example.core.domain.entities.filters.FilterCategory

class FiltersContainerAdapterDelegate :
    RecyclerViewAdapterDelegate<FilterCategory, LayoutFiltersContainerBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FilterCategory

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding> =
        FiltersContainerViewHolder(
            LayoutFiltersContainerBinding.inflate(inflater, parent, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<FilterCategory> =
        diffItemCallback

    private val diffItemCallback = object : DiffUtil.ItemCallback<FilterCategory>() {
        override fun areItemsTheSame(
            oldItem: FilterCategory,
            newItem: FilterCategory
        ): Boolean =
            oldItem.categoryName == newItem.categoryName

        override fun areContentsTheSame(
            oldItem: FilterCategory,
            newItem: FilterCategory
        ): Boolean =
            oldItem == newItem
    }

}

class FiltersContainerViewHolder(
    private val binding: LayoutFiltersContainerBinding
) : BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding>(binding) {
    private val adapter: BaseRecyclerViewAdapter = BaseRecyclerViewAdapter(
        listOf(FiltersAdapterDelegate())
    ).also {
        with(binding) {
            filtersContainerRecyclerView.adapter = it
            filtersContainerRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }
    }

    override fun onBind(item: FilterCategory) {
        adapter.submitList(item.filters as List<BaseRecyclerViewType>?)
    }

}

