package com.example.rawg.domain.mappers

import com.example.core.domain.entities.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.rawg.data.model.DevelopersResult
import com.example.rawg.data.model.Root
import com.example.rawg.data.model.interfaces.FilterParams
import javax.inject.Inject

class FiltersResponseMapper @Inject constructor() : Mapper<List<@JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>> {
    override fun map(value: List<FilterParams>?): List<Filter> =
        value?.map {
            Filter(it.name, it.slug)
        } ?: emptyList()
}
