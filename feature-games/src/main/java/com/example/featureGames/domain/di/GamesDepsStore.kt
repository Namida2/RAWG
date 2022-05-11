package com.example.featureGames.domain.di

import com.example.core.domain.interfaces.NavigationCallback
import com.example.dependencyDescription.domain.FeatureGamesDeps

object GamesDepsStore {
    var deps: FeatureGamesDeps? = null
    var navigationCallback: NavigationCallback? = null
    var gamesAppComponent: GamesAppComponent? = null
    get() = if(field == null) {
        field = DaggerGamesAppComponent.builder()
            .featureGamesDeps(deps).build()
        field
    } else field
}