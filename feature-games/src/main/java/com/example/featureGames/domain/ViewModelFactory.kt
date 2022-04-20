package com.example.featureGames.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.featureGames.domain.di.modules.GamesDepsStore.gamesAppComponent
import com.example.featureGames.presentation.GamesViewModel

class ViewModelFactory(val type: Int): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       val viewModel = when(modelClass) {
           GamesViewModel::class.java -> {
               // TODO: Provide useCases //STOPPED// 
               GamesViewModel(
                   gamesAppComponent.
               )
           }
           else -> {}
       }
        return viewModel as T
    }
}