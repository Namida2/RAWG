package com.example.featureGames.domain.model

import com.example.core.domain.tools.constants.Constants.FIRST_PAGE
import com.example.core.domain.tools.constants.Constants.PAGE_SIZE
import com.example.core.domain.tools.constants.RequestParams
import com.example.core.domain.tools.constants.RequestParams.PAGE
import com.example.featureGames.domain.model.interfaces.GetRequest
import java.text.SimpleDateFormat
import java.util.*

class GamesGetRequest: GetRequest {
    private var page: Int = FIRST_PAGE
    private var params = mutableMapOf<String, Any>()
    override fun next(): GamesGetRequest =
        this.copy().also {
            it.params[PAGE] = ++it.page
        }

    override fun getPage() = page
    override fun getParams() = params.toMap()
    fun copy(page: Int = this.page, params: Map<String, Any> = this.params): GamesGetRequest =
        GamesGetRequest().also { it.page = page; it.params = params.toMutableMap() }

    class Builder {
        private val separator = ","
        private var dates: String? = null
        private var platforms: String? = null
        private var stores: String? = null
        private var genres: String? = null
        private var developers: String? = null
        private var publishers: String? = null
        private var tags: String? = null
        private var metacritic: String? = null
        private var ordering: String? = null
        private var pageSize: Int = PAGE_SIZE
        private var page: Int = FIRST_PAGE

        companion object {
            private const val datePattern = "yyyy-MM-dd"
            private const val defaultTimeZoneName = "GMT"
            val defaultTimeZone: TimeZone = TimeZone.getTimeZone(defaultTimeZoneName)
        }

        fun setDates(startDate: Calendar, endDate: Calendar, timeZone: TimeZone = defaultTimeZone) =
            apply {
                dates = getDateString(endDate, timeZone) +
                        separator + getDateString(startDate, timeZone)
            }
        fun addPlatform(vararg platforms: String) = apply {
            this.platforms = platforms.joinToString(separator = separator)
        }
        fun addStores(vararg stores: String) = apply {
            this.stores = stores.joinToString(separator = separator)
        }
        fun addGenres(vararg genres: String) = apply {
            this.genres = genres.joinToString(separator = separator)
        }
        fun addDevelopers(vararg developers: String) = apply {
            this.developers = developers.joinToString(separator = separator)
        }
        fun addPublishers(vararg publishers: String) = apply {
            this.publishers = publishers.joinToString(separator = separator)
        }
        fun addTags(vararg tags: String) = apply {
            this.tags = tags.joinToString(separator = separator)
        }
        fun setMetacritic(minRating: Int, maxRating: Int) = apply {
            metacritic = "$minRating$separator$maxRating"
        }
        fun setPageSize(size: Int) = apply {
            this.pageSize = if(size > 0) size else this.pageSize
        }
        fun addOrdering(orderedField: String) = apply {
            this.ordering = orderedField
        }
        fun setPage(page: Int) = apply {
            this.page = page
        }
        fun build(): GamesGetRequest {
            val request = mutableMapOf<String, Any>()
            dates?.let { request[RequestParams.DATES] = it }
            platforms?.let { request[RequestParams.PLATFORMS] = it }
            stores?.let { request[RequestParams.STORES] = it }
            genres?.let { request[RequestParams.GENRES] = it }
            developers?.let { request[RequestParams.DEVELOPERS] = it }
            publishers?.let { request[RequestParams.PUBLISHERS] = it }
            tags?.let { request[RequestParams.TAGS] = it }
            metacritic?.let { request[RequestParams.METACRITIC] = it }
            ordering?.let { request[RequestParams.ORDERING] = it }
            request[RequestParams.PAGE_SIZE] = pageSize.toString()
            request[RequestParams.PAGE] = page
            return GamesGetRequest().also { it.params = request }
        }
        private fun getDateString(calendar: Calendar, timeZone: TimeZone) =
            SimpleDateFormat(datePattern, Locale.getDefault()).also {
                it.timeZone = timeZone
            }.format(calendar.time)
    }

}