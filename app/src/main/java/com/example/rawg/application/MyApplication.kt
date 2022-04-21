package com.example.rawg.application

import android.app.Application
import com.example.core.domain.tools.Messages
import com.example.core.domain.tools.Messages.API_KEY
import com.example.core.domain.tools.Messages.RAWG_BASE_URL
import com.example.featureGames.domain.di.GamesDepsStore
import com.example.featureGames.presentation.delegates.GamesDelegate
import com.example.rawg.domain.di.AppComponent
import com.example.rawg.domain.di.DaggerAppComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication: Application() {

    lateinit var _appComponent: AppComponent

    override fun onCreate() {
        _appComponent = DaggerAppComponent.builder().provideRetrofit(
            configureRetrofit()
        ).build()
        provideFeatureGamesDeps()
        super.onCreate()
    }

    private fun configureRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).addInterceptor(Interceptor { chain ->
                var request: Request = chain.request()
                val url: HttpUrl = request.url.newBuilder()
                    .addQueryParameter(Messages.PARAM_KEY, API_KEY).build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }).build()
        return Retrofit.Builder()
            .baseUrl(RAWG_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun provideFeatureGamesDeps() {
        GamesDepsStore.deps = _appComponent
    }
}