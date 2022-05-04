package com.example.core.data.entities.mappers

import com.example.core.data.entities.FilterEntity
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.interfaces.Mapper
import javax.inject.Inject

class FilterCategoryToFilterEntityMapper @Inject constructor() :
    Mapper<@JvmSuppressWildcards FilterCategory, List<@JvmSuppressWildcards FilterEntity>> {
    override fun map(value: FilterCategory): List<FilterEntity> =
        value.filters.map {
            FilterEntity(value.categoryName, it.name, it.slug)
        }


}