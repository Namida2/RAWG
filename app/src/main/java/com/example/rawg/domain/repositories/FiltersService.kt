package com.example.rawg.domain.repositories

import com.example.rawg.data.model.*
import retrofit2.http.GET

interface FiltersService {
    @GET("developers")
    suspend fun getDevelopers(): Root<DevelopersResult>

    @GET("genres")
    suspend fun getGenres(): Root<GenresResult>

    @GET("platforms")
    suspend fun getPlatforms(): Root<PlatformsResult>

    @GET("publishers")
    suspend fun getPublishers(): Root<PublishersResult>

    @GET("stores")
    suspend fun getStores(): Root<StoresResult>

    @GET("tags")
    suspend fun getTags(): Root<TagsResult>
}