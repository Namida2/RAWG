package com.example.core.domain.interfaces

import android.view.View
import androidx.fragment.app.Fragment

interface NavigationCallback {
    fun navigateTo(destination: Fragment, sharedEView: View? = null, tag: String = "" )
}