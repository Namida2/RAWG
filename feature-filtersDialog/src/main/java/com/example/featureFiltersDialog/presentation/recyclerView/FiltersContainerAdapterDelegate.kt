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
import java.lang.IllegalArgumentException

fun interface FiltersContainerAdapterDelegateCallback {
    fun onNewFilter(filterCategory: FilterCategory)
}

class FiltersContainerAdapterDelegate(
    private val callback: FiltersContainerAdapterDelegateCallback
) : RecyclerViewAdapterDelegate<FilterCategory, LayoutFiltersContainerBinding> {
    override val layoutId: Int
        get() = R.layout.layout_filter

    override fun isItMe(item: BaseRecyclerViewType): Boolean = item is FilterCategory

    override fun getViewHolder(
        inflater: LayoutInflater,
        container: ViewGroup
    ): BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding> =
        FiltersContainerViewHolder(
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

class FiltersContainerViewHolder(
    private val selectedBackground: Int,
    private val defaultDrawable: Int,
    private val callback: FiltersContainerAdapterDelegateCallback,
    private val binding: LayoutFiltersContainerBinding
) : BaseViewHolder<FilterCategory, LayoutFiltersContainerBinding>(binding) {

    private val onFilterSelectedCallback = FilterAdapterDelegateCallback {filter ->
        filterCategory!!.filters.indexOf(filter).also { position ->
            if (position == -1) throw IllegalArgumentException()
            val newFiltersList = filterCategory!!.filters.toMutableList()
            newFiltersList[position] = filter.copy(isSelected = !filter.isSelected)
            filterCategory!!.filters = newFiltersList
            callback.onNewFilter(filterCategory!!)
            adapter.submitList(newFiltersList as List<BaseRecyclerViewType>?)
        }
    }
    private var filterCategory: FilterCategory? = null
    private val adapter: BaseRecyclerViewAdapter = BaseRecyclerViewAdapter(
        listOf(FiltersAdapterDelegate(selectedBackground, defaultDrawable, onFilterSelectedCallback))
    ).also {
        with(binding) {
            filtersContainerRecyclerView.adapter = it
            filtersContainerRecyclerView.itemAnimator = null
            filtersContainerRecyclerView.layoutManager =
                StaggeredGridLayoutManager(FILTERS_SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        }
    }

    override fun onBind(item: FilterCategory) {
        filterCategory = item
        adapter.submitList(item.filters as List<BaseRecyclerViewType>?)
    }

}

