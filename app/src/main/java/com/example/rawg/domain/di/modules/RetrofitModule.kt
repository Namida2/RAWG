package com.example.rawg.domain.di.modules

import com.example.rawg.domain.repositories.FiltersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideFiltersService(retrofit: Retrofit): FiltersService =
        retrofit.create(FiltersService::class.java)
}