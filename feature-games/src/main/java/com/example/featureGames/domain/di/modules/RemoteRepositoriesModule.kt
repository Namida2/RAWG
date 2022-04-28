package com.example.featureGames.domain.di.modules

import android.content.Context
import com.example.featureGames.data.imageLoaders.GameImageLoader
import com.example.featureGames.data.imageLoaders.interfaces.ImagesLoader
import com.example.featureGames.data.entities.imageLoader.GameBackgroundImageUrlInfo
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
    fun provideImagesLoader(applicationContext: Context): ImagesLoader<GameBackgroundImageUrlInfo> =
        GameImageLoader(applicationContext)
}