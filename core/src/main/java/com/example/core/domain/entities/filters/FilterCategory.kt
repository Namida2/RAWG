package com.example.core.domain.entities.filters

import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType

data class FilterCategory(
    val categoryName: String,
    var filters: MutableList<BaseFilter>,
    var isSingleSelectable: Boolean = false
) : BaseRecyclerViewType {
    fun getSelectedFilters(): List<BaseFilter> =
        filters.filter { it.isSelected }
}