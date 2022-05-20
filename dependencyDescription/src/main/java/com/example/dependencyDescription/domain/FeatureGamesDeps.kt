package com.example.dependencyDescription.domain

import android.content.Context
import com.example.core.data.database.Database
import retrofit2.Retrofit

interface FeatureGamesDeps {
    val database: Database
    val retrofit: Retrofit
    val applicationContext: Context
}