package com.example.featureFiltersDialog.domain.di

import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.interfaces.OnNewGetRequestCallback
import com.example.dependencyDescription.domain.FeatureFiltersDeps

object FiltersDepsStore {
    var onNewRequestCallback: OnNewGetRequestCallback<GamesGetRequest>? = null
    var deps: FeatureFiltersDeps? = null
    var appComponent: FiltersAppComponent? = null
    get() = if(field == null) {
        field = DaggerFiltersAppComponent.builder()
            .featureFiltersDeps(deps).build()
        field
    } else field

    fun onCleared() {
        deps = null
        onNewRequestCallback = null
        appComponent = null
    }
}