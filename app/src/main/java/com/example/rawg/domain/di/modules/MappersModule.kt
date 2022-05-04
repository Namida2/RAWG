package com.example.rawg.domain.di.modules

import com.example.core.data.entities.FilterEntity
import com.example.core.data.entities.mappers.FilterCategoryToFilterEntityMapper
import com.example.core.data.entities.mappers.FilterEntitiesToFilterCategoriesMapper
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.mappers.FiltersResponseMapper
import dagger.Binds
import dagger.Module

@Module
interface MappersModule {
    @Binds
    fun provideFiltersResponseMapper(mapper: FiltersResponseMapper):
            Mapper<List<@JvmSuppressWildcards FilterParams>?, MutableList<@JvmSuppressWildcards Filter>>
    @Binds
    fun provideFilterCategoryToFilterEntityMapper(mapperFilterCategoryTo: FilterCategoryToFilterEntityMapper):
            Mapper<@JvmSuppressWildcards FilterCategory, List<@JvmSuppressWildcards FilterEntity>>
    @Binds
    fun provideFilterEntitiesToFilterCategoriesMapper(mapperFilterCategoryTo: FilterEntitiesToFilterCategoriesMapper):
            Mapper<List<@JvmSuppressWildcards FilterEntity>, MutableList<@JvmSuppressWildcards FilterCategory>>
}