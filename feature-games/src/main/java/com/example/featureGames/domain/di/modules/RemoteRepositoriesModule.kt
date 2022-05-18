package com.example.featureGames.domain.di.modules

import android.content.Context
import com.example.featureGames.data.imageLoaders.GameImageLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImageUrlInfo
import com.example.featureGames.domain.repositories.RAWGamesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteRepositoriesModule {
    @Provides
    @Singleton
    fun provideRAWGamesService(retrofit: Retrofit): RAWGamesService =
        retrofit.create(RAWGamesService::class.java)

    @Provides
    fun provideImagesLoader(applicationContext: Context): ImagesLoader<GameScreenshotUrlInfo> =
        GameImageLoader(applicationContext)
}