package com.example.featureFiltersDialog.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.StringConstants.UNKNOWN_VIEW_MODEL_CLASS
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore.appComponent
import com.example.featureFiltersDialog.presentation.FiltersViewModel

object ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            FiltersViewModel::class.java -> {
                FiltersViewModel(
                    appComponent!!.provideFiltersHolder()
                )
            }
            else -> throw IllegalArgumentException(UNKNOWN_VIEW_MODEL_CLASS + modelClass)
        }
        return viewModel as T
    }

}
