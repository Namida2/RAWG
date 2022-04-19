package com.example.rawg.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rawg.R
import com.example.rawg.domain.repositories.RAWGRemoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            ).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        HttpLoggingInterceptor()
        val service: RAWGRemoteRepository =
            retrofit.create(RAWGRemoteRepository::class.java)
        CoroutineScope(IO).launch {
            service.getGames()
        }

    }
}

