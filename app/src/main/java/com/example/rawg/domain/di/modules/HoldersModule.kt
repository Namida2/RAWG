package com.example.rawg.domain.di.modules

import com.example.core.domain.entities.filters.FiltersHolder
import com.example.core.domain.entities.filters.FiltersHolderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
 interface HoldersModule {
     @Binds
     @Singleton
     fun provideFiltersHolder(holder: FiltersHolderImpl): FiltersHolder
 }