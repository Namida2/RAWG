package com.example.featureFiltersDialog.domain.di

import com.example.core.domain.interfaces.remoteRepository.GetRequest
import com.example.featureFiltersDialog.domain.interfaces.OnNewRequestCallback

object FiltersDepsStore {
    var onNewRequest: OnNewRequestCallback<GetRequest>? = null
    var deps: FiltersAppComponentDeps? = null
    var appComponent: FiltersAppComponent? = null
    get() = if(field == null) {
        field = DaggerFiltersAppComponent.builder()
            .filtersAppComponentDeps(deps).build()
        field
    } else field

    fun onCleared() {
        deps = null
        onNewRequest = null
        appComponent = null
    }
}