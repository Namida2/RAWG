package com.example.rawg.domain.di

import android.content.Context
import com.example.core.domain.entities.Filter
import com.example.core.domain.interfaces.Mapper
import com.example.featureGames.domain.di.GamesAppComponentDeps
import com.example.rawg.data.model.interfaces.FilterParams
import com.example.rawg.domain.di.modules.MappersModule
import com.example.rawg.domain.di.modules.RetrofitModule
import com.example.rawg.domain.di.modules.UseCasesModule
import com.example.rawg.domain.useCases.ReadFiltersUseCase
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, UseCasesModule::class, MappersModule::class])
interface AppComponent: GamesAppComponentDeps {
    @Component.Builder
    interface Builder {
        fun putRetrofit(@BindsInstance retrofit: Retrofit): Builder
        fun putContext(@BindsInstance applicationContext: Context): Builder
        // TODO: Add Room database
        fun build(): AppComponent
    }
    fun provideReadFiltersUseCase(): ReadFiltersUseCase
}