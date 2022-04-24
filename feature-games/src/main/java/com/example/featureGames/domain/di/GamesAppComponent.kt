package com.example.featureGames.domain.di

import android.content.Context
import com.example.featureGames.domain.di.modules.RemoteRepositoryModule
import com.example.featureGames.domain.di.modules.UseCasesModule
import com.example.featureGames.domain.model.GamesHolder
import com.example.featureGames.domain.repositories.RAWGamesService
import com.example.featureGames.domain.useCase.GamesUseCase
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(
    dependencies = [GamesAppComponentDeps::class],
    modules = [RemoteRepositoryModule::class, UseCasesModule::class]
)
interface GamesAppComponent {
    fun provideRAWGamesService(): RAWGamesService
    fun provideTopPocksUseCase(): GamesUseCase
    fun provideGamesHolder(): GamesHolder
}

interface GamesAppComponentDeps {
    val applicationContext: Context
    val retrofit: Retrofit
}