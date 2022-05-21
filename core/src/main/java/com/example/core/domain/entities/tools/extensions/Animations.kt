package com.example.core.domain.entities.tools.extensions

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

fun View.prepareFadeInAnimation(
    duration: Long = 260,
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

fun View.prepareIncreaseHeightAnimation(
    duration: Long = 260,
    startDelay: Long = 0,
    interpolator: Interpolator,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
    return ObjectAnimator.ofPropertyValuesHolder(this, scaleX).apply {
        this.interpolator = interpolator
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}

fun View.prepareScaleAnimation(
    duration: Long = 260,
    startDelay: Long = 0,
    interpolator: Interpolator,
    startAlpha: Float = 0f,
    scale: Float = 0.8f,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, scale, 1f)
    val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, scale, 1f)
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, startAlpha, 1f)
    return ObjectAnimator.ofPropertyValuesHolder(this, scaleX, scaleY, alpha).apply {
        this.interpolator = interpolator
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}

fun View.startDefaultRecyclerViewItemAnimation(heightScale: Float = 16f) {
    this.prepareDefaultSpringAnimation(
        this.height / heightScale,
        springDampingRatio = SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY
    ).start()
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

fun View.prepareSlideDownFromTop(
    distance: Int,
    duration: Long = 260,
    startDelay: Long = 0,
    startAlpha: Float = 1f,
    doOnEnd: () -> Unit = {}
): ObjectAnimator {
    val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, startAlpha, 1f)
    val translationY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0f, distance.toFloat())
    return ObjectAnimator.ofPropertyValuesHolder(this, alpha, translationY).apply {
        interpolator = AccelerateInterpolator()
        this.duration = duration
        this.startDelay = startDelay
        doOnEnd { doOnEnd.invoke() }
    }
}

fun View.prepareSlideUpFromBottom(
    distance: Int,
    duration: Long = 260,
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