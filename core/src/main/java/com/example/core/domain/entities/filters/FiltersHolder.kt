package com.example.core.domain.entities.filters

import javax.inject.Inject

class FiltersHolderImpl @Inject constructor(): FiltersHolder {
    override var filters: MutableList<FilterCategory> =
        emptyList<FilterCategory>().toMutableList()
}

interface FiltersHolder {
    var filters: MutableList<FilterCategory>
}
