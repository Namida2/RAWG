package com.example.featureFiltersDialog.presentation

import androidx.lifecycle.ViewModel
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.core.domain.entities.filters.FilterCategoryName
import com.example.core.domain.entities.filters.FiltersHolder

class FiltersViewModel(
    private val filtersHolder: FiltersHolder
) : ViewModel() {

    fun getFilters(): List<BaseRecyclerViewType> {
        return prepareFiltersForView()
    }

    private fun prepareFiltersForView(): List<BaseRecyclerViewType> =
        filtersHolder.filters.map {
            listOf<BaseRecyclerViewType>(FilterCategoryName(it.categoryName)) + it
        }.flatten()

}