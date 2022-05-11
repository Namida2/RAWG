package com.example.core.domain.interfaces

import androidx.fragment.app.Fragment

interface NavigationCallback {
    fun navigateTo(destination: Fragment)
}