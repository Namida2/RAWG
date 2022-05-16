package com.example.featureGameDetails.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import com.example.core.presentaton.fragments.BaseFragment
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.core_game.domain.Game
import com.example.featureGameDetails.R
import com.example.featureGameDetails.databinding.FragmentGameDetailsBinding
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.domain.entities.ViewModelFactory
import com.example.featureGameDetails.presentation.viewPager.GamePageTransformer
import com.example.featureGameDetails.presentation.viewPager.adapterDelegates.GameScreenshotAdapterDelegate
import com.example.featureGameDetails.presentation.viewPager.itemDecorations.GameScreenshotsItemDecorations
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialSharedAxis
import kotlin.properties.Delegates

// TODO: Get details about the game, add queue, add fields to the Game entity //STOPPED//
class GameDetailsFragment : BaseFragment() {
    private var defaultScale by Delegates.notNull<Float>()
    private var currentPageMargin by Delegates.notNull<Int>()
    private var pageLargeMargin by Delegates.notNull<Int>()
    private var pageSmallMargin by Delegates.notNull<Int>()
    private var gameId by Delegates.notNull<Int>()
    private var binding: FragmentGameDetailsBinding? = null
    private val viewModel by viewModels<GameDetailsViewModel> { ViewModelFactory }
    private val adapter = BaseRecyclerViewAdapter(
        listOf(GameScreenshotAdapterDelegate())
    )
    companion object {
        const val GAME_ID_TAG = "GAME_ID_TAG"
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val outValue = TypedValue()
        gameId = arguments?.getInt(GAME_ID_TAG)!!
        resources.getValue(R.dimen.default_scale, outValue, true)
        defaultScale = outValue.float
        pageLargeMargin = resources.getDimensionPixelSize(R.dimen.page_large_margin)
        pageSmallMargin = resources.getDimensionPixelSize(R.dimen.page_small_margin)
        currentPageMargin = resources.getDimensionPixelSize(R.dimen.current_page_margin)
        viewModel.getDetails(gameId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            duration =
                resources.getInteger(com.example.core.R.integer.defaultAnimationDuration).toLong()
            scrimColor = ContextCompat.getColor(requireContext(), com.example.core.R.color.black)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGameDetailsBinding.inflate(inflater, container, false).let {
        binding = it; it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
            animateFirstVisit()
        }
        binding?.screenshotViewPager?.adapter = adapter
        binding?.screenshotViewPager?.offscreenPageLimit = 1
        binding?.screenshotViewPager?.addItemDecoration(
            GameScreenshotsItemDecorations(currentPageMargin)
        )
        binding?.screenshotViewPager?.setPageTransformer(
            GamePageTransformer(defaultScale, currentPageMargin, pageLargeMargin)
        )
        adapter.submitList(getPostImagesList())
        binding!!.ratingsContainer.setOnClickListener {
            animateFirstVisit()
        }
        iniViews(viewModel.getGameById(gameId))
    }

    private fun iniViews(game: Game) {
        with(binding!!) {
            metacriticTextView.text = game.metacritic.toString()
            ratingTextView.text = game.rating.toString()

        }
    }

    private fun animateFirstVisit() {
        startEnterSpringAnimation(
            listOf(
                binding!!.screenshotViewPager,
                binding!!.ratingsContainer,
                binding!!.progressContainer,
                binding!!.releasedAtContainer,
                binding!!.aboutContainer,
                binding!!.addedStatusContainer,
                binding!!.goToButtonsContainer,
                binding!!.genresRecyclerView,
                binding!!.tagsRecyclerView,
                binding!!.requirementsContainer,
                binding!!.storesRecyclerView,
            ).onEach { view -> view.alpha = 0f },
            springStartPosition = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition).toFloat(),
            delayBetweenAnimations = resources.getInteger(com.example.core.R.integer.smallAnimationDelay).toLong(),
            startDelay = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition).toLong(),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game_test))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game_test))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game_test))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game_test))),
        GameScreenshot(BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),)
    )

}