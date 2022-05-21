package com.example.core.domain.entities.tools.extensions

import android.util.Log

private const val TAG = "RAWG_LOGGING"
fun Any.logD(message: String) {
    Log.d(TAG, "$this: $message")
}

fun Any.logE(message: String) {
    Log.e(TAG, "$this: $message")
}