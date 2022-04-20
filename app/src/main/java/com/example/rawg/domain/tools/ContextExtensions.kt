package com.example.rawg.domain.tools

import android.content.Context
import com.example.rawg.application.MyApplication
import com.example.rawg.domain.di.AppComponent

val Context.appComponent: AppComponent
get() = when(this) {
    is MyApplication -> _appComponent
    else -> this.applicationContext.appComponent
}