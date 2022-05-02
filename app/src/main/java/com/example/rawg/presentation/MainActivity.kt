package com.example.rawg.presentation

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.core.domain.tools.constants.Constants.NUM_PAGES
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.logD
import com.example.featureGames.presentation.GamesFragment
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.ViewModelFactory
import com.example.rawg.domain.tools.appComponent
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : FragmentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private val gameScreenTags = GameScreenTags.values()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniBinding()
        getViewModel()
        observeViewModelStates()
        viewModel.readFilters()
    }

    private fun iniBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewPager.adapter = ScreenSlidePagerAdapter(this)
        // TODO: Remove commentaries and add OnScreenChangedListener
        binding.viewPager.offscreenPageLimit = NUM_PAGES
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = gameScreenTags[position].screenTag
        }.attach()
    }

    private fun getViewModel() {
        viewModel =
            ViewModelProvider(this, ViewModelFactory(appComponent))[MainViewModel::class.java]
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(this) {
            when (it) {
                is MainVMStates.LostNetworkConnection ->
                    createMessageAlertDialog(checkNetworkConnectionMessage)
                        ?.show(supportFragmentManager, "")
                is MainVMStates.FiltersLoadedSuccessfully -> {
                    setContentView(binding.root)
                }
                is MainVMStates.Error -> {
                    createMessageAlertDialog(it.message)
                        ?.show(supportFragmentManager, "")
                }
                is MainVMStates.Default -> {}
            }
        }
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

