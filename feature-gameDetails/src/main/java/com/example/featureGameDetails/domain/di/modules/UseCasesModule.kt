package com.example.featureGameDetails.domain.di.modules

import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCase
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCasesModule {
    @Binds
    fun provideGetGameDetailsUseCase(useCase: GetGameDetailsUseCaseImpl): GetGameDetailsUseCase
}