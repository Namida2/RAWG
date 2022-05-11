package com.example.dependencyDescription.domain

import android.content.Context
import retrofit2.Retrofit

interface FeatureGamesDeps {
    val applicationContext: Context
    val retrofit: Retrofit
}