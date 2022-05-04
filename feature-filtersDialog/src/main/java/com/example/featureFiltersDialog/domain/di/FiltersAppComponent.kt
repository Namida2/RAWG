package com.example.featureFiltersDialog.domain.di

import com.example.core.domain.entities.filters.FiltersHolder
import dagger.Component

@Component(dependencies = [FiltersAppComponentDeps::class])
interface FiltersAppComponent {
    fun provideFiltersHolder(): FiltersHolder
}

interface FiltersAppComponentDeps {
    val filtersHolder: FiltersHolder
}