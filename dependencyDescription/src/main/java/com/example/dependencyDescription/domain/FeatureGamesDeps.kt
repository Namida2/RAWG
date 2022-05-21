package com.example.dependencyDescription.domain

import android.content.Context
import com.example.core.data.database.Database
import com.example.core.domain.games.GamesHolder
import retrofit2.Retrofit

interface FeatureGamesDeps {
    val database: Database
    val retrofit: Retrofit
    val gamesHolder: GamesHolder
    val applicationContext: Context
}