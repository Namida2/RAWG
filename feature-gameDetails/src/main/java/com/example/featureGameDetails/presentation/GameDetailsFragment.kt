package com.example.featureGameDetails.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.entities.tools.constants.Constants.AVERAGE_FILTERS_LIST_SIZE
import com.example.core.domain.entities.tools.constants.Constants.DEFAULT_SPAN_COUNT
import com.example.core.domain.entities.tools.constants.Constants.DESCRIPTION_MAX_LINES
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.constants.StringConstants.COLON_SIGN
import com.example.core.domain.entities.tools.constants.StringConstants.HOURS
import com.example.core.domain.entities.tools.constants.StringConstants.PC_SLUG
import com.example.core.domain.entities.tools.extensions.createMessageAlertDialog
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.showIfNotAdded
import com.example.core.presentaton.dialogs.ClosedQuestionDialog
import com.example.core.presentaton.fragments.BaseFragment
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewAdapter
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.delegates.FiltersAdapterDelegate
import com.example.core.domain.games.Game
import com.example.featureGameDetails.R
import com.example.featureGameDetails.databinding.FragmentGameDetailsBinding
import com.example.featureGameDetails.databinding.LayoutRatingBinding
import com.example.featureGameDetails.domain.entities.GameScreenshot
import com.example.featureGameDetails.domain.entities.ViewModelFactory
import com.example.featureGameDetails.presentation.viewPager.GamePageTransformer
import com.example.featureGameDetails.presentation.viewPager.adapterDelegates.GameScreenshotAdapterDelegate
import com.example.featureGameDetails.presentation.viewPager.itemDecorations.GameScreenshotsItemDecorations
import com.google.android.material.transition.platform.MaterialContainerTransform
import kotlin.properties.Delegates

// TODO: Complete the GameDetailsScreen, add liking on the GameDetailsFragment,
//  complete filtering and searching //STOPPED//
class GameDetailsFragment : BaseFragment() {
    private var defaultScale by Delegates.notNull<Float>()
    private var currentPageMargin by Delegates.notNull<Int>()
    private var pageLargeMargin by Delegates.notNull<Int>()
    private var pageSmallMargin by Delegates.notNull<Int>()
    private val platformsAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private val genresAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private val tagsAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private val developersAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private val publishersAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private val storesAdapter = BaseRecyclerViewAdapter(getFiltersAdapterDelegate())
    private var gameId by Delegates.notNull<Int>()
    private var descriptionMaxLines = DESCRIPTION_MAX_LINES
    private var binding: FragmentGameDetailsBinding? = null
    private lateinit var viewModel: GameDetailsViewModel
    private val adapter = BaseRecyclerViewAdapter(
        listOf(GameScreenshotAdapterDelegate())
    )
    private var closedQuestionDialog = ClosedQuestionDialog<Unit>(
        onNegative = {

        }, onPositive = {

        })

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
        viewModel =
            ViewModelProvider(this, ViewModelFactory(gameId))[GameDetailsViewModel::class.java]
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
        observeViewModelEvents()
        observeViewModelStates()
        initViewPager()
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition(); animateFirstVisit() }
        binding!!.ratingsContainer.setOnClickListener {
            animateFirstVisit()
        }
    }

    private fun getFiltersAdapterDelegate(): List<FiltersAdapterDelegate> =
        listOf(
            FiltersAdapterDelegate(
                com.example.core.R.drawable.bg_black_lite_stroke_gradient_blue_to_pink_90,
                com.example.core.R.drawable.bg_black_lite_rounded,
            )
        )

    private fun initViewPager() {
        binding?.screenshotViewPager?.adapter = adapter
        binding?.screenshotViewPager?.offscreenPageLimit = 1
        binding?.screenshotViewPager?.addItemDecoration(
            GameScreenshotsItemDecorations(currentPageMargin)
        )
        binding?.screenshotViewPager?.setPageTransformer(
            GamePageTransformer(defaultScale, currentPageMargin, pageLargeMargin)
        )
        adapter.submitList(getPostImagesList())
    }

    private fun animateFirstVisit() {
        startEnterSpringAnimation(
            listOf(
                binding!!.appBar, binding!!.screenshotViewPager,
                binding!!.ratingsContainer, binding!!.progressContainer,
                binding!!.releasedInContainer, binding!!.aboutContainer,
                binding!!.addedStatusContainer, binding!!.goToButtonsContainer,
                binding!!.genresRecyclerView, binding!!.tagsRecyclerView,
                binding!!.requirementsContainer, binding!!.storesRecyclerView,
            ).onEach { view -> view.alpha = 0f },
            springStartPosition = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition)
                .toFloat(),
            delayBetweenAnimations = resources.getInteger(com.example.core.R.integer.smallAnimationDelay)
                .toLong(),
            startDelay = resources.getInteger(com.example.core.R.integer.defaultStartDelay)
                .toLong(),
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun observeViewModelEvents() {
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is GameDetailsVMEvents.NewScreenshotsListEvent -> {
                    adapter.submitList(it.value.getData() ?: return@observe)
                }
                is GameDetailsVMEvents.LostNetworkConnectionEvent -> {
                    it.value.getData() ?: return@observe
                    requireContext().createMessageAlertDialog(checkNetworkConnectionMessage)
                        ?.show(parentFragmentManager, "")
                }
                is GameDetailsVMEvents.OnError -> {
                    it.value.getData() ?: return@observe
                    closedQuestionDialog.showIfNotAdded(parentFragmentManager, "")
                }
            }
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is GameDetailsVMEStates.GameDetailsExists -> iniViews(it.game)
                is GameDetailsVMEStates.ReadingGameDetails -> Unit
                is GameDetailsVMEStates.Default -> Unit
            }
        }
    }

    // TODO: Continue to visualise the game details data //STOPPED//
    @SuppressLint("SetTextI18n")
    private fun iniViews(game: Game) {
        with(binding!!) {
            gameName.text =             game.gameEntity.name.toString()
            ratingTextView.text =       game.gameEntity.rating.toString()
            releasedInTextViw.text =    game.gameEntity.released.toString()
            metacriticTextView.text =   game.gameEntity.metacritic.toString()
            twitchCountTextView.text =  game.gameDetails?.gameDetailsEntity?.twitchCount.toString()
            redditCountTextView.text =  game.gameDetails?.gameDetailsEntity?.redditCount.toString()
            youtubeCountTextView.text = game.gameDetails?.gameDetailsEntity?.youtubeCount.toString()
            descriptionTextView.text =  game.gameDetails?.gameDetailsEntity?.description
            showMoreButton.setOnClickListener {
                descriptionMaxLines = if(descriptionMaxLines == DESCRIPTION_MAX_LINES) {
                    showMoreButton.text = resources.getString(R.string.showLess)
                    Int.MAX_VALUE
                } else {
                    showMoreButton.text = resources.getString(R.string.showMore)
                    DESCRIPTION_MAX_LINES
                }
                descriptionTextView.maxLines = descriptionMaxLines
            }
            yetStatusCount.text =       game.addedByStatus?.yet.toString()
            ownedStatusCount.text =     game.addedByStatus?.owned.toString()
            beatenStatusCount.text =    game.addedByStatus?.beaten.toString()
            toPlayStatusCount.text =    game.addedByStatus?.toplay.toString()
            playingStatusCount.text =   game.addedByStatus?.playing.toString()
            droppeStatusdCount.text =   game.addedByStatus?.dropped.toString()

            ratingTop.text =         game.gameEntity.ratingTop.toString()
            addedCount.text =        game.gameDetails?.gameDetailsEntity?.added.toString()
            reviewsCount.text =      game.gameDetails?.gameDetailsEntity?.reviewsCount.toString()
            playtime.text =          game.gameDetails?.gameDetailsEntity?.playtime.toString() + HOURS
            textReviewsCount.text =  game.gameDetails?.gameDetailsEntity?.reviewsTextCount.toString()
            achievementsCount.text = game.gameDetails?.gameDetailsEntity?.achievementsCount.toString()
            game.gameDetails?.platforms?.find {
                it.slug == PC_SLUG
            }.let {
                if(it == null) {
                    requirementsContainer.visibility = View.GONE
                    return@let
                }
                it.requirementsMinimum?.let { it -> requirementsMin.text = it }
                it.requirementsRecommended?.let { it -> requirementsRec.text = it }
            }
            showRatings(game)
            showFilters(game)
        }
    }

    private fun showFilters(game: Game) {
        with(binding!!) {
            showFilters(tagsContainer, tagsRecyclerView, tagsAdapter, viewModel.getGameTags(game))
            showFilters(storesContainer, storesRecyclerView, storesAdapter, viewModel.getGameStores(game))
            showFilters(genresContainer, genresRecyclerView, genresAdapter, viewModel.getGameGenres(game))
            showFilters(developersContainer, developersRecyclerView, developersAdapter, viewModel.getGameDevelopers(game))
            showFilters(publisherContainer, publishersRecyclerView, publishersAdapter, viewModel.getGamePublishers(game))
            showFilters(releasedInContainer, releasedInRecyclerView, platformsAdapter, viewModel.getReleasedInPlatforms(game))
        }
    }

    private fun showFilters(
        title: ViewGroup,
        container: RecyclerView,
        adapter: BaseRecyclerViewAdapter,
        filters: List<BaseRecyclerViewType>
    ) {
        if (filters.isEmpty()) {
            title.visibility = View.GONE
            return
        }
        container.adapter = adapter
        container.layoutManager = if (filters.size > AVERAGE_FILTERS_LIST_SIZE)
            StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.HORIZONTAL)
        else LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        adapter.submitList(filters)
    }

    @SuppressLint("SetTextI18n")
    private fun showRatings(game: Game) {
        if (game.gameDetails?.ratings.isNullOrEmpty()) return
        val largestValue = game.gameDetails?.ratings?.first()?.count?.toFloat() ?: return
        logD(game.gameDetails?.ratings.toString())
        game.gameDetails?.ratings?.forEach { rating ->
            val ratingLayout = LayoutRatingBinding.inflate(layoutInflater, binding!!.root, false)
            ratingLayout.ratingTitle.text = "${rating.title}$COLON_SIGN"
            ratingLayout.ratingCount.text = rating.count.toString()
            ratingLayout.root.doOnPreDraw {
                val width = ratingLayout.progress.width / (largestValue / if (rating.count == 0) 1 else rating.count)
                ratingLayout.progress.layoutParams = LinearLayout.LayoutParams(
                    width.toInt(), ratingLayout.progress.height
                )
            }
            binding!!.progressContainer.addView(ratingLayout.root)
        }
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(
            BitmapFactory.decodeResource(resources, com.example.core.R.drawable.ic_screenshot_placeholder)
        )
    )

}