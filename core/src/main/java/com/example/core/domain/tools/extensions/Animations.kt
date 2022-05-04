package com.example.core.domain.tools.extensions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd

fun View.prepareFadeInAnimation (
    duration: Long = 300,
    startDelay: Long = 0,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    return ObjectAnimator.ofPropertyValuesHolder(this, alpha).apply {
        interpolator = LinearInterpolator()
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}