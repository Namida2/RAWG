package com.example.featureFiltersDialog.domain

import javax.inject.Inject
import javax.inject.Singleton

class FiltersHolderImpl @Inject constructor(): FiltersHolder {
    override val filters: MutableList<FiltersRecyclerViewType> =
        emptyList<FiltersRecyclerViewType>().toMutableList()
}

interface FiltersHolder {
    val filters: MutableList<FiltersRecyclerViewType>
}
