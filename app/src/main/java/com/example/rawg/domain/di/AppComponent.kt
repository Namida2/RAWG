package com.example.rawg.domain.di

import android.content.Context
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
        fun putRetrofit(@BindsInstance retrofit: Retrofit): Builder
        fun putContext(@BindsInstance applicationContext: Context): Builder
        // TODO: Add Room database
        fun build(): AppComponent
    }
}