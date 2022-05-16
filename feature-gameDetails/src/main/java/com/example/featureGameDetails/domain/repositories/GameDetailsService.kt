package com.example.featureGameDetails.domain.repositories

import com.example.featureGameDetails.data.Root
import retrofit2.http.GET
import retrofit2.http.Path

interface GameDetailsService {
    @GET("games/{id}")
    suspend fun getDevelopers(@Path("id") id: Int): Root
}