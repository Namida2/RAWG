package com.example.core.domain.di.modules

import com.example.core.data.database.Database
import com.example.core.data.database.daos.CashInfoDao
import com.example.core.data.database.daos.FiltersDao
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideFiltersDao(database: Database): FiltersDao =
        database.getFiltersDao()
    @Provides
    fun provideCashInfoDao(database: Database): CashInfoDao =
        database.getCashInfoDao()
}