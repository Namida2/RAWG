package com.example.featureGamesViewPager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.core.domain.tools.constants.Constants
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureFiltersDialog.presentation.FiltersBottomSheetDialog
import com.example.featureGames.presentation.GamesFragment
import com.example.featureGamesViewPager.databinding.FragmentGamesViewPagerBinding
import com.example.featureGamesViewPager.presentation.viewPager.GamePagerAdapter
import com.example.featureGamesViewPager.presentation.viewPager.getCurrentFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialFadeThrough

class GamesViewPagerFragment : Fragment(), View.OnClickListener {
    private var binding: FragmentGamesViewPagerBinding? = null
    private val gameScreenTags = GameScreenTags.values()
    private val onBackPressedCallback = object : androidx.activity.OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding == null || binding!!.viewPager.currentItem == 0) {
                isEnabled = false
                activity?.onBackPressed()
            }
            binding!!.viewPager.currentItem = binding!!.viewPager.currentItem - 1
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
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initViewPager()
        binding?.filtersCardView?.setOnClickListener(this)
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
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
