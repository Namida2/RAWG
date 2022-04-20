package com.example.featureGames.domain.di.modules

import com.example.featureGames.domain.repositories.RAWGamesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RemoteRepositoryModule {
    @Provides
    @Singleton
    fun provideRAWGamesService(retrofit: Retrofit): RAWGamesService =
        retrofit.create(RAWGamesService::class.java)

}