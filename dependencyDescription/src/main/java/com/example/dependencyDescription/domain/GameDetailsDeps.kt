package com.example.dependencyDescription.domain

import com.example.core_game.domain.GamesHolder
import retrofit2.Retrofit

interface GameDetailsDeps {
    val retrofit: Retrofit
    val gamesHolder: GamesHolder
}