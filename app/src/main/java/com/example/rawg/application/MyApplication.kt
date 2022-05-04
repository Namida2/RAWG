package com.example.rawg.application

import android.app.Application
import androidx.room.Room
import com.example.core.data.database.Database
import com.example.core.domain.entities.NetworkConnectionListener
import com.example.core.domain.tools.constants.RequestParams.API_KEY
import com.example.core.domain.tools.constants.RequestParams.PARAM_KEY
import com.example.core.domain.tools.constants.RequestParams.RAWG_BASE_URL
import com.example.featureGames.domain.di.GamesDepsStore
import com.example.core.R
import com.example.rawg.domain.di.AppComponent
import com.example.rawg.domain.di.DaggerAppComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    lateinit var _appComponent: AppComponent

    override fun onCreate() {
        NetworkConnectionListener.registerCallback(applicationContext)
        _appComponent = DaggerAppComponent.builder()
            .putRetrofit(configureRetrofit())
            .putContext(applicationContext)
            .putDatabase(
                Room.databaseBuilder(
                    applicationContext,
                    Database::class.java,
                    resources.getString(R.string.databaseName)
                ).build()
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
                    .addQueryParameter(PARAM_KEY.slug, API_KEY.slug).build()
                request = request.newBuilder().url(url).build()
                chain.proceed(request)
            }).build()
        return Retrofit.Builder()
            .baseUrl(RAWG_BASE_URL.slug)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun provideFeatureGamesDeps() {
        GamesDepsStore.deps = _appComponent
    }
}