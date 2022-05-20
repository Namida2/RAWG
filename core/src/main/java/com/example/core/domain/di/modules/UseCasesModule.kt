package com.example.core.domain.di.modules

import com.example.core.domain.games.useCases.LikeGameUseCase
import com.example.core.domain.games.useCases.LikeGameUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface UseCasesModule {
    @Binds
    @Singleton
    fun provideLikeGameUseCase(useCaseImpl: LikeGameUseCaseImpl): LikeGameUseCase
}