package com.example.featureFiltersDialog.domain

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class FiltersRecyclerViewType(
    val filterName: String, val filters: List<Filter>
): BaseRecyclerViewType