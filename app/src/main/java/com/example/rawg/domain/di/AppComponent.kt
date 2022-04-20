package com.example.rawg.domain.di

import com.example.featureGames.domain.di.GamesDependencies
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Component
@Singleton
interface AppComponent: GamesDependencies {
    @Component.Builder
    interface Builder {
        fun provideRetrofit(@BindsInstance retrofit: Retrofit): Builder
        // TODO: Add Room database
        fun build(): AppComponent
    }
}