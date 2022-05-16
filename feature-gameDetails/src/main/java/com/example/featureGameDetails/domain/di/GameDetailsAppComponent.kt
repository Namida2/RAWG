package com.example.featureGameDetails.domain.di

import com.example.dependencyDescription.domain.GameDetailsDeps
import com.example.featureGameDetails.domain.di.modules.RemoteRepositoriesModule
import com.example.featureGameDetails.domain.di.modules.UseCasesModule
import com.example.featureGameDetails.domain.useCases.GetGameDetailsUseCase
import dagger.Component

@Component(dependencies = [GameDetailsDeps::class], modules = [UseCasesModule::class, RemoteRepositoriesModule::class])
interface GameDetailsAppComponent {
    fun provideGetGameDetailsUseCase(): GetGameDetailsUseCase
}
