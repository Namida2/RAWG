package com.example.rawg.domain.di

import com.example.featureGames.domain.di.GamesAppComponentDeps
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Component
@Singleton
interface AppComponent: GamesAppComponentDeps {
    @Component.Builder
    interface Builder {
        fun provideRetrofit(@BindsInstance retrofit: Retrofit): Builder
        // TODO: Add Room database
        fun build(): AppComponent
    }
}