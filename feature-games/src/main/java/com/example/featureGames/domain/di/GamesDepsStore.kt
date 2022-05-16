package com.example.featureGames.domain.di

import com.example.core.domain.interfaces.NavigationCallback
import com.example.dependencyDescription.domain.FeatureGamesDeps
import com.example.dependencyDescription.domain.GameDetailsDeps
import com.example.featureGameDetails.domain.di.GameDetailsDepsStore

object GamesDepsStore {
    var deps: FeatureGamesDeps? = null
    var navigationCallback: NavigationCallback? = null
    var gamesAppComponent: GamesAppComponent? = null
    get() = if(field == null) {
        field = DaggerGamesAppComponent.builder()
            .featureGamesDeps(deps).build()
        GameDetailsDepsStore.deps = field
        field
    } else field
}