package com.example.featureGames.domain.model

class GameRequest {
    private val request: Map<String, String> = mapOf()

    class Builder {
        private val delimiter = ","
        private var startDate: String? = null
        private var endDate: String? = null
        private var platforms: String? = null
        private var stores: String? = null
        private var genres: String? = null
        private var developers: String? = null
        private var publishers: String? = null
        private var tags: String? = null

        fun setStartDate(date: String) {
            startDate = date
        }

        fun setEndDate(date: String) {
            endDate = date
        }

        fun addPlatform(vararg platforms: String) = apply {
            platforms.forEach { this.platforms += it + delimiter }
        }

        fun addStores(vararg stores: String) = apply {
            stores.forEach { this.stores += it + delimiter }
        }

        fun addGenres(vararg genres: String) = apply {
            genres.forEach { this.genres += it + delimiter }
        }

        fun addDevelopers(vararg developers: String) = apply {
            developers.forEach { this.developers += it + delimiter }
        }

        fun addPublishers(vararg publishers: String) = apply {
            publishers.forEach { this.publishers += it + delimiter }
        }

        fun addTags(vararg tags: String) = apply {
            tags.forEach { this.tags += it + delimiter }
        }

//        fun build() = let {
//
//        }
    }

//    fun getRequest(): Map<String, String> {
//
//    }
}