package com.example.rawg.presentation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.core.domain.tools.constants.Constants.GAME_SCREEN_TAG
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.presentation.GamesFragment
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.tools.Constants.NUM_PAGES
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : FragmentActivity() {
    lateinit var binding: ActivityMainBinding
    private val gameScreenTags = GameScreenTags.values()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        binding.viewPager.offscreenPageLimit = NUM_PAGES
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = gameScreenTags[position].screenTag
        }.attach()
        setContentView(binding.root)
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment = run {
            logD("ScreenSlidePagerAdapter, screenTag: ${gameScreenTags[position]}")
            GamesFragment().also {
                it.arguments = bundleOf(
                    it.hashCode().toString() to gameScreenTags[position]
                )
            }
        }
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) super.onBackPressed()
        else binding.viewPager.currentItem = binding.viewPager.currentItem - 1
    }

}

