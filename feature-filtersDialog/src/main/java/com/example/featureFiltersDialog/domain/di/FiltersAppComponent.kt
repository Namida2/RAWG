package com.example.featureFiltersDialog.domain.di

import com.example.core.domain.entities.filters.FiltersHolder
import com.example.dependencyDescription.domain.FeatureFiltersDeps
import dagger.Component

@Component(dependencies = [FeatureFiltersDeps::class])
interface FiltersAppComponent {
    fun provideFiltersHolder(): FiltersHolder
}