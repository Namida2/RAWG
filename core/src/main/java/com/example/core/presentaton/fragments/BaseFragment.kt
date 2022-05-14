package com.example.core.presentaton.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.example.core.domain.tools.extensions.prepareDefaultSpringAnimation
import com.example.core.domain.tools.extensions.prepareScaleAnimation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseFragment : Fragment() {
    open fun startEnterSpringAnimation(
        views: List<View>,
        springStartPosition: Float,
        delayBetweenAnimations: Long = 0,
        springFinalPosition: Float = 0f
    ) {
        MainScope().launch {
            views.forEach { view ->
                view.prepareDefaultSpringAnimation(springStartPosition, springFinalPosition).start()
                view.prepareScaleAnimation().start()
                delay(delayBetweenAnimations)
            }
        }
    }
}