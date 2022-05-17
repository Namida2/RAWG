package com.example.featureGameDetails.domain.di

import com.example.dependencyDescription.domain.GameDetailsDeps
import com.example.featureGameDetails.domain.di.modules.MappersModule
import com.example.featureGameDetails.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCaseFactory
import dagger.Component

@Component(dependencies = [GameDetailsDeps::class], modules = [RemoteRepositoriesModule::class, MappersModule::class])
interface GameDetailsAppComponent {
    fun provideGetGameDetailsUseCaseImplFactory(): GetGameDetailsUseCaseFactory
}
