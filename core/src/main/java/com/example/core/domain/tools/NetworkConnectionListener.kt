package com.example.core.domain.tools

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NetworkConnectionListener {
    private lateinit var connectivityManager: ConnectivityManager
    private val _networkConnectionChanges = MutableSharedFlow<Boolean>(replay = 1)
    val networkConnectionChanges: SharedFlow<Boolean> = _networkConnectionChanges

    private var currentState = false
    fun registerCallback(context: Context) {
        if(this::connectivityManager.isInitialized) return
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                currentState = true
                _networkConnectionChanges.tryEmit(true)
            }
            override fun onLost(network: Network) {
                currentState = false
                _networkConnectionChanges.tryEmit(false)
            }
        })
    }
    fun isNetworkConnected(): Boolean = currentState
}