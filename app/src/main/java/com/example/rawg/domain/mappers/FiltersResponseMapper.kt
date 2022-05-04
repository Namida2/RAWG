package com.example.rawg.domain.mappers

import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.data.model.interfaces.FilterParams
import javax.inject.Inject

class FiltersResponseMapper @Inject constructor() :
    Mapper<List<@JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>> {
    override fun map(value: List<FilterParams>?): MutableList<Filter> =
        value?.mapNotNull {
            if (it.name.isNullOrEmpty() || it.slug.isNullOrEmpty()) null
            else Filter(it.name!!, it.slug!!)
        }?.toMutableList() ?: mutableListOf()
}
