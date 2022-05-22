package com.example.featureGameDetails.domain.di

import com.example.core.domain.di.modules.DatabaseModule
import com.example.core.domain.di.modules.UseCasesModule
import com.example.core.domain.games.useCases.LikeGameUseCase
import com.example.dependencyDescription.domain.GameDetailsDeps
import com.example.featureGameDetails.domain.di.modules.MappersModule
import com.example.featureGameDetails.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCaseFactory
import dagger.Component
import javax.inject.Singleton

@Component(
    dependencies = [GameDetailsDeps::class],
    modules = [
        RemoteRepositoriesModule::class,
        MappersModule::class,
        UseCasesModule::class,
        DatabaseModule::class
    ]
)
@Singleton
interface GameDetailsAppComponent {
    fun provideGetGameDetailsUseCaseImplFactory(): GetGameDetailsUseCaseFactory
    fun provideLikeGameUseCase(): LikeGameUseCase
}
