package com.example.rawg.domain.useCases

import com.example.core.domain.interfaces.Mapper
import com.example.core.domain.tools.constants.RequestParams
import com.example.core.domain.tools.extensions.logD
import com.example.featureFiltersDialog.domain.Filter
import com.example.featureFiltersDialog.domain.FiltersHolder
import com.example.featureFiltersDialog.domain.FiltersRecyclerViewType
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.repositories.FiltersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import javax.inject.Inject

class ReadFiltersUseCaseImpl @Inject constructor(
    private val filtersService: FiltersService,
    private val filtersHolder: FiltersHolder,
    private val mapper: Mapper<List<@JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>>,
) : ReadFiltersUseCase {
    override suspend fun getFilters(
        coroutineScope: CoroutineScope,
    ): FiltersHolder {

        val developersResult = coroutineScope.async { filtersService.getDevelopers() }
        val genresResult = coroutineScope.async { filtersService.getGenres() }
        val platformsResult = coroutineScope.async { filtersService.getPlatforms() }
        val publishersResult = coroutineScope.async { filtersService.getPublishers() }
        val storesResult = coroutineScope.async { filtersService.getStores() }
        val tagsResult = coroutineScope.async { filtersService.getTags() }

        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.DEVELOPERS,
                mapper.map(developersResult.await().results).also { logD(it.toString()) })
        )
        mapper.map(developersResult.await().results).also { logD(it.toString()) }
        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.GENRES,
                mapper.map(genresResult.await().results).also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.PLATFORMS,
                mapper.map(platformsResult.await().results).also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.PUBLISHERS,
                mapper.map(publishersResult.await().results).also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.STORES,
                mapper.map(storesResult.await().results).also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FiltersRecyclerViewType(
                RequestParams.TAGS,
                mapper.map(tagsResult.await().results).also { logD(it.toString()) }
            )
        )
        return filtersHolder
    }
}

interface ReadFiltersUseCase {
    suspend fun getFilters(coroutineScope: CoroutineScope): FiltersHolder
}
