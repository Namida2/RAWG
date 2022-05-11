package com.example.featureGamesViewPager.domain.di

import com.example.core.domain.interfaces.NavigationCallback
import com.example.dependencyDescription.domain.FeatureFiltersDeps
import com.example.dependencyDescription.domain.FeatureGamesDeps
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureGames.domain.di.GamesDepsStore

object GamesViewPagerDepsStore {
    var navigationCallback: NavigationCallback? = null
    set(value) {
        GamesDepsStore.navigationCallback = value
        field = value
    }
    var deps: FeatureGamesViewPagerDeps? = null
    set(value) {
        GamesDepsStore.deps = value
        FiltersDepsStore.deps = value
        field = value
    }
}

interface FeatureGamesViewPagerDeps: FeatureGamesDeps, FeatureFiltersDeps

