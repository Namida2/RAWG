package com.example.core.domain.entities.filters

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class FilterCategory(
    val categoryName: String, val filters: MutableList<Filter>
): BaseRecyclerViewType