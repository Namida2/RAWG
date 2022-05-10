package com.example.rawg.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.Constants.NUM_PAGES
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.prepareFadeInAnimation
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureFiltersDialog.presentation.FiltersBottomSheetDialog
import com.example.featureGames.presentation.GamesFragment
import com.example.rawg.databinding.ActivityMainBinding
import com.example.rawg.domain.ViewModelFactory
import com.example.rawg.domain.tools.appComponent
import com.example.rawg.presentation.viewPager.GamePagerAdapter
import com.example.rawg.presentation.viewPager.getCurrentFragment
import com.google.android.material.tabs.TabLayoutMediator

// TODO: App a GamesViewPagerFragment, GameScreenDetails and like games //STOPPED//
class MainActivity : AppCompatActivity(), View.OnClickListener {
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

    private fun getViewModel() {
        viewModel = ViewModelProvider(
            this, ViewModelFactory(appComponent)
        )[MainViewModel::class.java]
    }

    private fun iniBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        initViewPager()
        binding.filtersCardView.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        FiltersDepsStore.deps = appComponent
        FiltersDepsStore.onNewRequestCallback = (binding.viewPager
            .getCurrentFragment<GamesFragment>(supportFragmentManager))?.getOnNewRequestCallback()
        FiltersBottomSheetDialog.getNewInstance()
            ?.show(supportFragmentManager, "")
    }

    private fun initViewPager() {
        with(binding) {
            viewPager.adapter = GamePagerAdapter(this@MainActivity, gameScreenTags)
            viewPager.offscreenPageLimit = NUM_PAGES
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = gameScreenTags[position].screenTag
            }.attach()
        }
    }

    override fun onBackPressed() {
        if (binding.viewPager.currentItem == 0) super.onBackPressed()
        else binding.viewPager.currentItem = binding.viewPager.currentItem - 1
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(this) {
            when (it) {
                is MainVMStates.LostNetworkConnection ->
                    createMessageAlertDialog(checkNetworkConnectionMessage)
                        ?.show(supportFragmentManager, "")
                is MainVMStates.FiltersLoadedSuccessfully -> {
                    setContentView(binding.root)
                    binding.root.doOnPreDraw { rootView ->
                        rootView.prepareFadeInAnimation().start()
                    }
                }
                is MainVMStates.Error -> {
                    createMessageAlertDialog(it.message)
                        ?.show(supportFragmentManager, "")
                }
                MainVMStates.ReadingFilters -> {}
                is MainVMStates.Default -> {}
            }
        }
    }

}

