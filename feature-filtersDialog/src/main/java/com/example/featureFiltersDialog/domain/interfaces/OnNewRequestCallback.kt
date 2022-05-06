package com.example.featureFiltersDialog.domain.interfaces

import com.example.core.domain.interfaces.remoteRepository.GetRequest

interface OnNewRequestCallback<T: GetRequest> {
    fun onNewRequest(request :T)
}