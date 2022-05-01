package com.example.rawg.domain.useCases

import com.example.core.domain.entities.RequestFilters
import com.example.core.domain.entities.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.core.domain.tools.extensions.logD
import com.example.rawg.data.model.*
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.repositories.FiltersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReadFiltersUseCaseImpl @Inject constructor(
    private val filtersService: FiltersService,
    private val requestFilters: RequestFilters,
    private val mapper: Mapper<List< @JvmSuppressWildcards FilterParams>?, List<@JvmSuppressWildcards Filter>>,
) : ReadFiltersUseCase {
    override suspend fun getFilters(coroutineScope: CoroutineScope): RequestFilters {
        requestFilters.developers = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getDevelopers().results }
        ).also { logD(it.toString()) }
        requestFilters.genres = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getGenres().results }
        ).also { logD(it.toString()) }
        requestFilters.platforms = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getPlatforms().results  }
        ).also { logD(it.toString()) }
        requestFilters.publishers = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getPublishers().results }
        ).also { logD(it.toString()) }
        requestFilters.stores = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getStores().results  }
        ).also { logD(it.toString()) }
        requestFilters.tags = mapper.map(
            withContext(coroutineScope.coroutineContext) { filtersService.getTags().results  }
        ).also { logD(it.toString()) }

        return requestFilters
    }
}

interface ReadFiltersUseCase {
    suspend fun getFilters(coroutineScope: CoroutineScope): RequestFilters
}
