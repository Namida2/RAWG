package com.example.core.data.entities.mappers

import com.example.core.data.entities.FilterEntity
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.interfaces.Mapper
import javax.inject.Inject

class FilterEntitiesToFilterCategoriesMapper @Inject constructor():
    Mapper<List<@JvmSuppressWildcards FilterEntity>, MutableList<@JvmSuppressWildcards FilterCategory>> {
    override fun map(value: List<FilterEntity>): MutableList<FilterCategory> {
        val filtersHolder = mutableListOf<FilterCategory>()
        value.forEach {
            getFilterCategory(it.categoryName, filtersHolder)
                .filters.add(Filter(
                    id = it.id.toString(),
                    name = it.name,
                    categoryName = it.categoryName))

        }
        return filtersHolder
    }

    private fun getFilterCategory(
        categoryName: String,
        filtersHolder: MutableList<FilterCategory>
    ): FilterCategory {
        filtersHolder.find {
            it.categoryName == categoryName
        }.also {
            if (it != null) return it
            val newFilterCategory = FilterCategory(categoryName, mutableListOf())
            filtersHolder.add(newFilterCategory)
            return newFilterCategory
        }
    }
}