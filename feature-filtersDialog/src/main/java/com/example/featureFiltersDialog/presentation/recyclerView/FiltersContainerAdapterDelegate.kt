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
import com.example.featureFiltersDialog.domain.FiltersRecyclerViewType

class FiltersContainerAdapterDelegate :
    RecyclerViewAdapterDelegate<FiltersRecyclerViewType, LayoutFiltersContainerBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FiltersRecyclerViewType

    override fun getViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): BaseViewHolder<FiltersRecyclerViewType, LayoutFiltersContainerBinding> =
        FiltersContainerViewHolder(
            LayoutFiltersContainerBinding.inflate(inflater, parent, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<FiltersRecyclerViewType> =
        diffItemCallback

    private val diffItemCallback = object : DiffUtil.ItemCallback<FiltersRecyclerViewType>() {
        override fun areItemsTheSame(
            oldItem: FiltersRecyclerViewType,
            newItem: FiltersRecyclerViewType
        ): Boolean =
            oldItem.filterName == newItem.filterName

        override fun areContentsTheSame(
            oldItem: FiltersRecyclerViewType,
            newItem: FiltersRecyclerViewType
        ): Boolean =
            oldItem == newItem
    }

}

class FiltersContainerViewHolder(
    private val binding: LayoutFiltersContainerBinding
) : BaseViewHolder<FiltersRecyclerViewType, LayoutFiltersContainerBinding>(binding) {
    private val adapter: BaseRecyclerViewAdapter = BaseRecyclerViewAdapter(
        listOf(FiltersAdapterDelegate())
    ).also {
        with(binding) {
            filtersContainerRecyclerView.adapter = it
            filtersContainerRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        }
    }

    override fun onBind(item: FiltersRecyclerViewType) {
        adapter.submitList(item.filters)
    }

}

