package com.example.featureGameDetails.presentation.viewPager

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class GamePageTransformer(
    private val scale: Float,
    private val currentPageMargin: Int,
    private val leftAndRightPageMargin: Int,
) : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.translationX = -(currentPageMargin + leftAndRightPageMargin) * position
        page.scaleY = 1 - (scale * abs(position))
    }
}