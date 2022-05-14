package com.example.core.domain.tools.extensions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.core.animation.doOnEnd
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

fun View.prepareFadeInAnimation (
    duration: Long = 360,
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

fun View.prepareScaleAnimation (
    duration: Long = 360,
    startDelay: Long = 0,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.8f, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.8f, 1f)
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
    return ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY, alpha).apply {
        interpolator = OvershootInterpolator()
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}

fun View.prepareDefaultSpringAnimation(
    springStartPosition: Float,
    springFinalPosition: Float = 0f,
    springStiffness: Float = SpringForce.STIFFNESS_VERY_LOW,
    springDampingRatio: Float = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
): SpringAnimation =
    SpringAnimation(this, DynamicAnimation.TRANSLATION_Y, springFinalPosition).apply {
        setStartValue(springStartPosition)
        spring.stiffness = springStiffness
        spring.dampingRatio = springDampingRatio
}

fun View.prepareSlideUpFromBottom(
    distance: Int,
    duration: Long = 250,
    startDelay: Long = 0,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, distance.toFloat(), 0f)
    return ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        interpolator = LinearInterpolator()
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}