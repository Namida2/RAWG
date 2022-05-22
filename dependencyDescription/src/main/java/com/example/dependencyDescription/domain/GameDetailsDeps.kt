package com.example.dependencyDescription.domain

import com.example.core.data.database.Database
import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.domain.games.GamesHolder
import retrofit2.Retrofit

interface GameDetailsDeps {
    val retrofit: Retrofit
    val gamesHolder: GamesHolder
    val database: Database
    val imagesLoader: ImagesLoader<GameScreenshotUrlInfo>
}