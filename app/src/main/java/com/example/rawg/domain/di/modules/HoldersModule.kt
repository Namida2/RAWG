package com.example.rawg.domain.di.modules

import com.example.featureFiltersDialog.domain.FiltersHolder
import com.example.featureFiltersDialog.domain.FiltersHolderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
 interface HoldersModule {
     @Binds
     @Singleton
     fun provideFiltersHolder(holder: FiltersHolderImpl): FiltersHolder
 }