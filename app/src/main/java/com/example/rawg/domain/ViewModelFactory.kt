package com.example.rawg.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.StringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.rawg.domain.di.AppComponent
import com.example.rawg.presentation.MainViewModel

class ViewModelFactory(private val appComponent: AppComponent) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            MainViewModel::class.java ->
                MainViewModel(appComponent.provideReadFiltersUseCase())
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS + modelClass)
        }
        return viewModel as T
    }
}