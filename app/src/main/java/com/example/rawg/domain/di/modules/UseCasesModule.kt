package com.example.rawg.domain.di.modules

import com.example.rawg.domain.useCases.ReadFiltersUseCase
import com.example.rawg.domain.useCases.ReadFiltersUseCaseImpl
import com.example.rawg.domain.useCases.ReadGamesFromLocalStorageUseCase
import com.example.rawg.domain.useCases.ReadGamesFromLocalStorageUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun provideReadFiltersUseCase(useCase: ReadFiltersUseCaseImpl): ReadFiltersUseCase
}