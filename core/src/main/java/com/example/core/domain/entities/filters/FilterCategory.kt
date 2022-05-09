package com.example.core.domain.entities.filters

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class FilterCategory(
    val categoryName: String,
    var filters: MutableList<Filter>,
    var isSingleSelectable: Boolean = false
) : BaseRecyclerViewType {
    fun getSelectedFilters(): List<Filter> =
        filters.filter { it.isSelected }
}