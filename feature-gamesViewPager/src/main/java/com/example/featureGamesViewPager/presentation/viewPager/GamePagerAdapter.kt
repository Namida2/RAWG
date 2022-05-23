package com.example.featureGamesViewPager.presentation.viewPager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.core.domain.entities.tools.constants.Constants
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.extensions.logD
import com.example.featureGames.presentation.GamesFragment
import com.example.featureGames.presentation.GamesFragment.Companion.SCREEN_TAG

class GamePagerAdapter(fa: FragmentActivity, private val gameScreenTags: Array<GameScreenTags>) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = Constants.NUM_PAGES
    override fun createFragment(position: Int): Fragment = run {
        logD("ScreenSlidePagerAdapter, screenTag: ${gameScreenTags[position]}")
        GamesFragment().also {
            it.arguments = bundleOf(
                SCREEN_TAG to gameScreenTags[position]
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun<FragmentClass> ViewPager2.getCurrentFragment(fragmentManager: FragmentManager): FragmentClass? =
    fragmentManager.findFragmentByTag("f$currentItem") as? FragmentClass

