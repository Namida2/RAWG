package com.example.core.domain.entities.tools.extensions

import android.view.View
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Fragment.startEnterSpringAnimation(
    views: List<View>,
    springStartPosition: Float,
    startDelay: Long = 0,
    delayBetweenAnimations: Long = 0,
    springFinalPosition: Float = 0f,
    interpolator: Interpolator = OvershootInterpolator(),
    duration: Long = 360
) {
    MainScope().launch {
        delay(startDelay)
        views.forEach { view ->
            view.prepareDefaultSpringAnimation(springStartPosition, springFinalPosition).start()
            view.prepareScaleAnimation(duration, interpolator = interpolator).start()
            delay(delayBetweenAnimations)
        }
    }
}