package com.example.rawg.domain.mappers

import com.example.core.domain.entities.filters.Filter
import com.example.rawg.data.model.interfaces.FilterParams
import javax.inject.Inject

class FilterParamsToFiltersMapperImpl @Inject constructor() :
    FilterParamsToFiltersMapper {
    override fun map(categoryName: String, params: List<FilterParams>?): MutableList<Filter> =
        params?.mapNotNull {
            if (it.name.isNullOrEmpty() || it.slug.isNullOrEmpty()) null
            else Filter(
                id = it.id.toString(),
                name = it.name!!,
                categoryName = categoryName
            )
        }?.toMutableList() ?: mutableListOf()
}

interface FilterParamsToFiltersMapper {
    fun map(categoryName: String, params: List<FilterParams>?): MutableList<Filter>
}
