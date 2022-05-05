package com.example.rawg.presentation.viewPager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.core.domain.tools.constants.Constants
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.presentation.GamesFragment

class GamePagerAdapter(fa: FragmentActivity, private val gameScreenTags: Array<GameScreenTags>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = Constants.NUM_PAGES
    override fun createFragment(position: Int): Fragment = run {
        logD("ScreenSlidePagerAdapter, screenTag: ${gameScreenTags[position]}")
        GamesFragment().also {
            it.arguments = bundleOf(
                it.hashCode().toString() to gameScreenTags[position]
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun<FragmentClass> ViewPager2.getCurrentFragment(fragmentManager: FragmentManager): FragmentClass? =
    fragmentManager.findFragmentByTag("f$currentItem") as? FragmentClass

