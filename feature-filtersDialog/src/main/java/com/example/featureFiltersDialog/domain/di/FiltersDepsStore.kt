package com.example.featureFiltersDialog.domain.di

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.interfaces.remoteRepository.GetRequest
import com.example.core.domain.interfaces.OnNewGetRequestCallback

object FiltersDepsStore {
    var onNewRequestCallback: OnNewGetRequestCallback<GamesGetRequest>? = null
    var deps: FiltersAppComponentDeps? = null
    var appComponent: FiltersAppComponent? = null
    get() = if(field == null) {
        field = DaggerFiltersAppComponent.builder()
            .filtersAppComponentDeps(deps).build()
        field
    } else field

    fun onCleared() {
        deps = null
        onNewRequestCallback = null
        appComponent = null
    }
}