package com.example.rawg.domain.useCases

import androidx.core.content.contentValuesOf
import com.example.core.domain.entities.Filter
import com.example.core.domain.entities.RequestFilters
import com.example.core.domain.interfaces.Mapper
import com.example.core.domain.tools.extensions.logD
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.repositories.FiltersService
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ReadFiltersUseCaseImpl @Inject constructor(
    private val filtersService: FiltersService,
    private val requestFilters: RequestFilters,
    private val mapper: Mapper<List<@JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>>,
) : ReadFiltersUseCase {
    override suspend fun getFilters(
        coroutineScope: CoroutineScope,
    ): RequestFilters {

        val developersResult = coroutineScope.async() { filtersService.getDevelopers() }
        val genresResult = coroutineScope.async() { filtersService.getGenres() }
        val platformsResult = coroutineScope.async() { filtersService.getPlatforms() }
        val publishersResult = coroutineScope.async() { filtersService.getPublishers() }
        val storesResult = coroutineScope.async() { filtersService.getStores() }
        val tagsResult = coroutineScope.async() { filtersService.getTags() }
//
        coroutineScope.launch {
            requestFilters.developers =
                mapper.map(developersResult.await().results).also { logD(it.toString()) }
            requestFilters.genres =
                mapper.map(genresResult.await().results).also { logD(it.toString()) }
            requestFilters.platforms =
                mapper.map(platformsResult.await().results).also { logD(it.toString()) }
            requestFilters.publishers =
                mapper.map(publishersResult.await().results).also { logD(it.toString()) }
            requestFilters.stores =
                mapper.map(storesResult.await().results).also { logD(it.toString()) }
            requestFilters.tags =
                mapper.map(tagsResult.await().results).also { logD(it.toString()) }
        }
        return requestFilters
    }
}

interface ReadFiltersUseCase {
    suspend fun getFilters(coroutineScope: CoroutineScope): RequestFilters
}
