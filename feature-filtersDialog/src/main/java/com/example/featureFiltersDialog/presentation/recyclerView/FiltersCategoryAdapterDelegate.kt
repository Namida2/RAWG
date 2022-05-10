package com.example.featureFiltersDialog.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.tools.constants.Constants.FILTERS_SPAN_COUNT
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.BaseViewHolder
import com.example.core.presentaton.recyclerView.RecyclerViewAdapterDelegate
import com.example.featureFiltersDialog.R
import com.example.featureFiltersDialog.databinding.LayoutFiltersContainerBinding

fun interface FiltersContainerAdapterDelegateCallback {
    fun onNewFilter(filterCategory: FilterCategory)
}

class FiltersCategoryAdapterDelegate(
    private val callback: FiltersContainerAdapterDelegateCallback
) : RecyclerViewAdapterDelegate<FilterCategory, LayoutFiltersContainerBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FilterCategory

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding> =
        FiltersCategoryViewHolder(
            com.example.core.R.drawable.bg_black_lite_stroke_gradient_blue_to_pink_90,
            com.example.core.R.drawable.bg_black_lite_rounded,
            callback,
            LayoutFiltersContainerBinding.inflate(inflater, container, false)
        )

    override fun getDiffItemCallback(): DiffUtil.ItemCallback<FilterCategory> =
        diffItemCallback

    private val diffItemCallback = object : DiffUtil.ItemCallback<FilterCategory>() {
        override fun areItemsTheSame(
            oldItem: FilterCategory,
            newItem: FilterCategory
        ): Boolean = oldItem.categoryName == newItem.categoryName

        override fun areContentsTheSame(
            oldItem: FilterCategory,
            newItem: FilterCategory
        ): Boolean = oldItem == newItem
    }
}

class FiltersCategoryViewHolder(
    selectedBackground: Int,
    defaultDrawable: Int,
    private val callback: FiltersContainerAdapterDelegateCallback,
    private val binding: LayoutFiltersContainerBinding
) : BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding>(binding) {
    private var filterCategory: FilterCategory? = null
    private var positionOfLastSelectedFilter = -1
    private val onFilterSelectedCallback = FilterAdapterDelegateCallback { filter ->
        with(filterCategory!!) {
            this.filters.indexOf(filter).also { position ->
                if (position == -1) throw IllegalArgumentException()
                val newFiltersList = this.filters.toMutableList()
                newFiltersList[position] = filter.copy(isSelected = !filter.isSelected)
                this.filters = newFiltersList
                if (this.isSingleSelectable) {
                    funDeselectLastFilter()
                    positionOfLastSelectedFilter =
                        if(position == positionOfLastSelectedFilter) -1 else position
                }
                callback.onNewFilter(this)
                adapter.submitList(this.filters as List<BaseRecyclerViewType>)
            }
        }
    }
    private val adapter: BaseRecyclerViewAdapter = BaseRecyclerViewAdapter(
        listOf(
            FiltersAdapterDelegate(
                selectedBackground,
                defaultDrawable,
                onFilterSelectedCallback
            )
        )
    ).also {
        with(binding) {
            filtersContainerRecyclerView.adapter = it
            filtersContainerRecyclerView.setHasFixedSize(true)
            filtersContainerRecyclerView.itemAnimator = null
            filtersContainerRecyclerView.layoutManager =
                StaggeredGridLayoutManager(
                    FILTERS_SPAN_COUNT,
                    StaggeredGridLayoutManager.HORIZONTAL
                )
        }
    }

    override fun onBind(item: FilterCategory) {
        filterCategory = item.also { category ->
            if (category.isSingleSelectable)
                positionOfLastSelectedFilter =
                    category.filters.indexOfFirst { it.isSelected }
        }
        adapter.submitList(item.filters as List<BaseRecyclerViewType>?)
    }

    private fun funDeselectLastFilter() {
        if (positionOfLastSelectedFilter == -1) return
        with(filterCategory!!) {
            val lastSelectedFilter = this.filters[positionOfLastSelectedFilter]
            val newFiltersList = this.filters.toMutableList()
            newFiltersList[positionOfLastSelectedFilter] = lastSelectedFilter.copy(
                isSelected = false
            )
            this.filters = newFiltersList
        }
    }
}

