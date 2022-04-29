package com.example.rawg.presentation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.core.domain.tools.constants.Constants.GAME_SCREEN_TAG
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.featureGames.presentation.GamesFragment
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.tools.Constants.NUM_PAGES

class MainActivity : FragmentActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        setContentView(binding.root)
    }
    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> {
                GamesFragment().also {
                    it.arguments = bundleOf(
                        GAME_SCREEN_TAG to GameScreenTags.TOP_PICKS
                    )
                }
            }
            1 -> {
                GamesFragment().also {
                    it.arguments = bundleOf(
                        GAME_SCREEN_TAG to GameScreenTags.ALL_GAMES
                    )
                }
            }
            else -> {
                GamesFragment()
            }
        }
    }


    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) super.onBackPressed()
         else binding.viewPager.currentItem = binding.viewPager.currentItem - 1
    }

}

