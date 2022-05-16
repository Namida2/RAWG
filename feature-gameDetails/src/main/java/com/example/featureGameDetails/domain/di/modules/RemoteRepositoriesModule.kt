package com.example.featureGameDetails.domain.di.modules

import com.example.featureGameDetails.domain.repositories.GameDetailsService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class RemoteRepositoriesModule {
    @Provides
    fun provideGameDetailsService(retrofit: Retrofit): GameDetailsService =
        retrofit.create(GameDetailsService::class.java)
}