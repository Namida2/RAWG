package com.example.rawg.domain.mappers

import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.data.model.interfaces.FilterParams
import javax.inject.Inject

class FilterParamsToFiltersMapperImpl @Inject constructor() :
    FilterParamsToFiltersMapper{
    override fun map(categoryName: String, params: List<FilterParams>?): MutableList<Filter> =
        params?.mapNotNull {
            if (it.name.isNullOrEmpty() || it.slug.isNullOrEmpty()) null
            else Filter(categoryName, it.name!!, it.slug!!)
        }?.toMutableList() ?: mutableListOf()
}

interface FilterParamsToFiltersMapper {
    fun map(categoryName: String, params: List<FilterParams>?): MutableList<Filter>
}
