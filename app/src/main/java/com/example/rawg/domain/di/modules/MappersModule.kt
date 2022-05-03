package com.example.rawg.domain.di.modules

import com.example.featureFiltersDialog.domain.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.mappers.FiltersResponseMapper
import dagger.Binds
import dagger.Module

@Module
interface MappersModule {
    @Binds
    fun provideFiltersResponseMapper(mapper: FiltersResponseMapper):  Mapper<List<@JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>>
}