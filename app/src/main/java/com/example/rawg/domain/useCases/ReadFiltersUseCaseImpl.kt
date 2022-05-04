package com.example.rawg.domain.useCases

import com.example.core.data.database.daos.CashInfoDao
import com.example.core.data.database.daos.FiltersDao
import com.example.core.data.entities.CashInfo
import com.example.core.data.entities.FilterEntity
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.entities.filters.FiltersHolder
import com.example.core.domain.interfaces.Mapper
import com.example.core.domain.tools.constants.RequestParams
import com.example.core.domain.tools.extensions.logD
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.repositories.FiltersService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class ReadFiltersUseCaseImpl @Inject constructor(
    private val filtersDao: FiltersDao,
    private val cashInfoDao: CashInfoDao,
    private val filtersService: FiltersService,
    private val filtersHolder: FiltersHolder,
    private val filtersResponseToFilterMapper: Mapper<List<FilterParams>?, MutableList<Filter>>,
    private val filterCategoryToFilterEntityMapper: Mapper<FilterCategory, List<FilterEntity>>,
    private val filterEntitiesToFilterCategoriesMapper:  Mapper<List<FilterEntity>, MutableList<FilterCategory>>
) : ReadFiltersUseCase {
    private val oneDayMillis = 86_400_000L
    override suspend fun getFilters(
        coroutineScope: CoroutineScope,
    ): FiltersHolder {
        val lastCashedAt = cashInfoDao.readCashInfo()?.lastCashedAt ?: 0
        val currentTime = System.currentTimeMillis()
        val dif = currentTime - lastCashedAt
        if (dif > oneDayMillis) {
            logD("ReadFiltersUseCaseImpl: read new filters")
            readFiltersFromRemoteDataSource(coroutineScope)
            filtersDao.deleteAldAndInsertNewFilters(
                filtersHolder.filters.map {
                    filterCategoryToFilterEntityMapper.map(it)
                }.flatten()
            )
            cashInfoDao.deleteAldAndSetNewCashInfo(CashInfo(currentTime))
        } else {
            logD("ReadFiltersUseCaseImpl: read existing filters")
            readFiltersFromLocalDataSource()
        }
        return filtersHolder
    }

    private suspend fun readFiltersFromRemoteDataSource(coroutineScope: CoroutineScope) {
        val developersResult = coroutineScope.async { filtersService.getDevelopers() }
        val genresResult = coroutineScope.async { filtersService.getGenres() }
        val platformsResult = coroutineScope.async { filtersService.getPlatforms() }
        val publishersResult = coroutineScope.async { filtersService.getPublishers() }
        val storesResult = coroutineScope.async { filtersService.getStores() }
        val tagsResult = coroutineScope.async { filtersService.getTags() }

        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.DEVELOPERS.myName,
                filtersResponseToFilterMapper.map(developersResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.GENRES.myName,
                filtersResponseToFilterMapper.map(genresResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.PLATFORMS.myName,
                filtersResponseToFilterMapper.map(platformsResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.PUBLISHERS.myName,
                filtersResponseToFilterMapper.map(publishersResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.STORES.myName,
                filtersResponseToFilterMapper.map(storesResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
        filtersHolder.filters.add(
            FilterCategory(
                RequestParams.TAGS.myName,
                filtersResponseToFilterMapper.map(tagsResult.await().results)
                    .also { logD(it.toString()) }
            )
        )
    }

    private suspend fun readFiltersFromLocalDataSource() {
        filtersHolder.filters = filterEntitiesToFilterCategoriesMapper.map(
            filtersDao.getFilters()
        )
    }
}

interface ReadFiltersUseCase {
    suspend fun getFilters(coroutineScope: CoroutineScope): FiltersHolder
}
