package com.example.core.domain.interfaces

import com.example.core.domain.interfaces.remoteRepository.GetRequest

interface OnNewGetRequestCallback<T: GetRequest> {
    fun onNewRequest(request :T)
}