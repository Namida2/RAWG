package com.example.rawg.domain.mappers

import com.example.core.domain.entities.filters.BaseFilter
import com.example.rawg.data.model.interfaces.FilterParams
import javax.inject.Inject

class FilterParamsToFiltersMapperImpl @Inject constructor() :
    FilterParamsToFiltersMapper {
    override fun map(categoryName: String, params: List<FilterParams>?): MutableList<BaseFilter> =
        params?.mapNotNull {
            if (it.name.isNullOrEmpty() || it.slug.isNullOrEmpty()) null
            else BaseFilter(
                id = it.id.toString(),
                name = it.name!!,
                categoryName = categoryName,
                slug = it.slug
            )
        }?.toMutableList() ?: mutableListOf()
}

interface FilterParamsToFiltersMapper {
    fun map(categoryName: String, params: List<FilterParams>?): MutableList<BaseFilter>
}
