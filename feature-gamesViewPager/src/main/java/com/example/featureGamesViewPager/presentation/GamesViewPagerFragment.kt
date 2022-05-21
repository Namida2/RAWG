package com.example.featureGamesViewPager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import com.example.core.domain.entities.tools.constants.Constants
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.presentaton.fragments.BaseFragment
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureFiltersDialog.presentation.FiltersBottomSheetDialog
import com.example.featureGames.presentation.GamesFragment
import com.example.featureGamesViewPager.databinding.FragmentGamesViewPagerBinding
import com.example.featureGamesViewPager.presentation.viewPager.GamePagerAdapter
import com.example.featureGamesViewPager.presentation.viewPager.getCurrentFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialSharedAxis

class GamesViewPagerFragment : BaseFragment(), View.OnClickListener {
    private var enterAnimationWasPlayed = false
    private var binding: FragmentGamesViewPagerBinding? = null
    private val gameScreenTags = GameScreenTags.values()
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding == null || binding!!.viewPager.currentItem == 0) {
                isEnabled = false
                activity?.onBackPressed()
            }
            binding!!.viewPager.currentItem = binding!!.viewPager.currentItem - 1
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        FragmentGamesViewPagerBinding.inflate(inflater, container, false).let {
            binding = it; it.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        binding?.filtersCardView?.setOnClickListener(this)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
        binding!!.toolBar.setOnClickListener {
            binding!!.toolBar.alpha = 0f
            binding!!.tabLayout.alpha = 0f
            binding!!.viewPager.alpha = 0f
            animateFirstVisit()
        }
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
            if (!enterAnimationWasPlayed) {
                animateFirstVisit()
            }
        }
    }


    private fun animateFirstVisit() {
        enterAnimationWasPlayed = true
        startEnterSpringAnimation(
            listOf(binding!!.toolBar, binding!!.tabLayout, binding!!.viewPager)
                .onEach { view -> view.alpha = 0f },
            springStartPosition = resources.getInteger(com.example.core.R.integer.defaultSpringStartPosition).toFloat(),
            delayBetweenAnimations = resources.getInteger(com.example.core.R.integer.defaultAnimationDelay).toLong()
        )
    }

    override fun onClick(v: View?) {
//        FiltersDepsStore.deps = appComponent
        FiltersDepsStore.onNewRequestCallback = (binding?.viewPager
            ?.getCurrentFragment<GamesFragment>(parentFragmentManager))?.getOnNewRequestCallback()
        FiltersBottomSheetDialog.getNewInstance()
            ?.show(parentFragmentManager, "")
    }

    private fun initViewPager() {
        with(binding!!) {
            viewPager.adapter = GamePagerAdapter(requireActivity(), gameScreenTags)
            viewPager.offscreenPageLimit = Constants.NUM_PAGES
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = gameScreenTags[position].screenTag
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
