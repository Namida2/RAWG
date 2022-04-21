package com.example.featureGames.domain.di

object GamesDepsStore {
    var deps: GamesAppComponentDeps? = null
    var gamesAppComponent: GamesAppComponent? = null
    get() = if(field == null) {
        field = DaggerGamesAppComponent.builder().gamesAppComponentDeps(deps).build()
        field
    } else field
}