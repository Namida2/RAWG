package com.example.featureGameDetails.domain.di

import com.example.dependencyDescription.domain.GameDetailsDeps

object GameDetailsDepsStore {
    var deps: GameDetailsDeps? = null
    var appComponent: GameDetailsAppComponent? = null
        get() = if(field == null) {
            field = DaggerGameDetailsAppComponent.builder()
                .gameDetailsDeps(deps).build()
            field
        } else field
}