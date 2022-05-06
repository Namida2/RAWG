package com.example.rawg.domain.di.modules

import com.example.core.data.entities.FilterEntity
import com.example.core.data.entities.mappers.FilterCategoryToFilterEntityMapper
import com.example.core.data.entities.mappers.FilterEntitiesToFilterCategoriesMapper
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.domain.mappers.FilterParamsToFiltersMapper
import com.example.rawg.domain.mappers.FilterParamsToFiltersMapperImpl
import dagger.Binds
import dagger.Module

@Module
interface MappersModule {
    @Binds
    fun provideFilterParamsToFiltersMapper(mapper: FilterParamsToFiltersMapperImpl): FilterParamsToFiltersMapper
    @Binds
    fun provideFilterCategoryToFilterEntityMapper(mapperFilterCategoryTo: FilterCategoryToFilterEntityMapper):
            Mapper<@JvmSuppressWildcards FilterCategory, List<@JvmSuppressWildcards FilterEntity>>
    @Binds
    fun provideFilterEntitiesToFilterCategoriesMapper(mapperFilterCategoryTo: FilterEntitiesToFilterCategoriesMapper):
            Mapper<List<@JvmSuppressWildcards FilterEntity>, MutableList<@JvmSuppressWildcards FilterCategory>>
}