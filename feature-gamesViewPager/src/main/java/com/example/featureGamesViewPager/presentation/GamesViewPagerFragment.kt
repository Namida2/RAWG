package com.example.featureGamesViewPager.presentation

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.constants.Constants
import com.example.core.domain.entities.tools.constants.StringConstants.EMPTY_STRING
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.entities.tools.extensions.prepareScaleAnimation
import com.example.core.domain.entities.tools.extensions.startEnterSpringAnimation
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore
import com.example.featureFiltersDialog.presentation.FiltersBottomSheetDialog
import com.example.featureGames.presentation.GamesFragment
import com.example.featureGamesViewPager.R
import com.example.featureGamesViewPager.databinding.FragmentGamesViewPagerBinding
import com.example.featureGamesViewPager.presentation.viewPager.GamePagerAdapter
import com.example.featureGamesViewPager.presentation.viewPager.getCurrentFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialSharedAxis

class GamesViewPagerFragment : Fragment(), View.OnClickListener {
    private var enterAnimationWasPlayed = false
    private var isFilterIcon = true
    private var isMyLikesScreen = false
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

    companion object {
        const val GAMES_VIEW_PAGER_FRAGMENT_TAG = "GAMES_VIEW_PAGER_FRAGMENT_TAG"
        private var currentViewPagerPosition = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration =
                resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
        }
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration =
                resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
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
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
            if (!enterAnimationWasPlayed) {
                animateFirstVisit()
            }
        }
        onSearchTextChanged(binding!!.searchEditText.text)
        binding!!.searchEditText.doAfterTextChanged(::onSearchTextChanged)
    }


    private fun onSearchTextChanged(text: Editable?) {
        text ?: return
        if (isEmptyField(text.toString())) {
            isFilterIcon = true
            changeFilterContainerIcon(R.drawable.ic_filter)
        } else if (isFilterIcon) {
            isFilterIcon = false
            changeFilterContainerIcon(R.drawable.ic_check)
        }
    }

    private fun changeFilterContainerIcon(resourceId: Int) {
        binding!!.filterIcon.prepareScaleAnimation(
            duration = resources.getInteger(com.example.core.R.integer.smallAnimationDuration)
                .toLong(),
            startScale = 1f,
            endScale = 0f,
            startAlpha = 1f
        ) {
            binding?.filterIcon?.setImageResource(resourceId)
            binding?.filterIcon?.prepareScaleAnimation(
                duration = resources.getInteger(com.example.core.R.integer.smallAnimationDuration)
                    .toLong(),
                startScale = 0f,
                endScale = 1f,
                startAlpha = 1f
            )?.start()
        }.start()
    }

    private fun animateFirstVisit() {
        enterAnimationWasPlayed = true
        startEnterSpringAnimation(
            listOf(binding!!.toolBar, binding!!.tabLayout, binding!!.viewPager)
                .onEach { view -> view.alpha = 0f },
            springStartPosition = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition)
                .toFloat(),
            delayBetweenAnimations = resources.getInteger(com.example.core.R.integer.defaultAnimationDelay)
                .toLong()
        )
    }

    override fun onClick(v: View?) {
        if (isMyLikesScreen) {
            showSnackBar(com.example.core.R.string.unavailableActionMessage)
            return
        }
        val currentFragment =
            (binding?.viewPager?.getCurrentFragment<GamesFragment>(parentFragmentManager))
        if (isFilterIcon) {
            FiltersDepsStore.onNewRequestCallback = currentFragment?.getOnNewRequestCallback()
            FiltersBottomSheetDialog.getNewInstance()?.show(parentFragmentManager, "")
        } else {
            currentFragment?.makeNewRequest(
                GamesGetRequest.Builder()
                    .setName(binding!!.searchEditText.text.toString())
                    .build()
            )
            binding!!.searchEditText.setText(EMPTY_STRING)
        }
    }

    private fun showSnackBar(messageStringId: Int) {
        Snackbar.make(binding!!.root, messageStringId, Snackbar.LENGTH_LONG)
            .setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    com.example.core.R.color.black
                )
            )
            .setTextColor(ContextCompat.getColor(requireContext(), com.example.core.R.color.white))
            .show()
    }

    private fun initViewPager() {
        with(binding!!) {
            viewPager.adapter = GamePagerAdapter(requireActivity(), gameScreenTags)
            viewPager.offscreenPageLimit = Constants.NUM_PAGES
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = gameScreenTags[position].screenTag
            }.attach()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    currentViewPagerPosition = position
                    isMyLikesScreen = when (gameScreenTags[position]) {
                        GameScreenTags.MY_LIKES -> true
                        else -> false
                    }
                }
            })
            viewPager.setCurrentItem(currentViewPagerPosition, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
