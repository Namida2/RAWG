package com.example.rawg.domain.di

import android.content.Context
import com.example.featureFiltersDialog.domain.di.FiltersAppComponentDeps
import com.example.featureGames.domain.di.GamesAppComponentDeps
import com.example.rawg.domain.di.modules.HoldersModule
import com.example.rawg.domain.di.modules.MappersModule
import com.example.rawg.domain.di.modules.RetrofitModule
import com.example.rawg.domain.di.modules.UseCasesModule
import com.example.rawg.domain.useCases.ReadFiltersUseCase
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, UseCasesModule::class, MappersModule::class, HoldersModule::class])
interface AppComponent: GamesAppComponentDeps, FiltersAppComponentDeps {
    @Component.Builder
    interface Builder {
        fun putRetrofit(@BindsInstance retrofit: Retrofit): Builder
        fun putContext(@BindsInstance applicationContext: Context): Builder
        // TODO: Add Room database
        fun build(): AppComponent
    }
    fun provideReadFiltersUseCase(): ReadFiltersUseCase
}