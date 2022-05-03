package com.example.featureFiltersDialog.domain.di

object FiltersDepsStore {
    var deps: FiltersAppComponentDeps? = null
    var appComponent: FiltersAppComponent? = null
    get() = if(field == null) {
        field = DaggerFiltersAppComponent.builder()
            .filtersAppComponentDeps(deps).build()
        field
    } else field

    fun onCleared() = Unit

}