package com.example.featureGameDetails.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
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
import com.example.core.domain.entities.tools.extensions.*
import com.example.core.domain.games.Game
import com.example.core.presentaton.dialogs.ClosedQuestionDialog
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewAdapter
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.core.presentaton.recyclerView.delegates.FiltersAdapterDelegate
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

class GameDetailsFragment : Fragment() {
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
    private var isLiked = false
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
            containerColor =
                ContextCompat.getColor(requireContext(), com.example.core.R.color.black)
            endContainerColor =
                ContextCompat.getColor(requireContext(), com.example.core.R.color.black)
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
        view.doOnPreDraw {
            startPostponedEnterTransition()
            animateFirstVisit(
                preparePlaceholderViews()
            )
        }
        binding!!.likeCardView.setOnClickListener {
            viewModel.currentGame ?: return@setOnClickListener
            isLiked = !isLiked
            viewModel.onLikeButtonClick(isLiked)
            setLikeColorFilters(binding!!.likeIcon, isLiked)
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

    private fun animateFirstVisit(views: List<View>) {
        startEnterSpringAnimation(
            views.onEach { view -> view.alpha = 0f },
            springStartPosition = resources.getInteger(
                com.example.core.R.integer.smallSpringStartPosition
            ).toFloat(),
            delayBetweenAnimations = resources.getInteger(
                com.example.core.R.integer.smallAnimationDelay
            ).toLong(),
            startDelay = resources.getInteger(
                com.example.core.R.integer.defaultStartDelay
            ).toLong(),
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
                    ClosedQuestionDialog<Unit> {
                        viewModel.getDetails(gameId)
                    }.show(parentFragmentManager, "")
                }
            }
        }
    }

    private fun observeViewModelStates() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is GameDetailsVMEStates.GameDetailsExists -> onGameDetailsExists(it.game)
                is GameDetailsVMEStates.ReadingGameDetails -> Unit
                is GameDetailsVMEStates.Default -> Unit
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun onGameDetailsExists(game: Game) {
        with(binding!!) {
            isLiked = game.gameEntity.isLiked
            setLikeColorFilters(likeIcon, game.gameEntity.isLiked)
            gameName.text = game.gameEntity.name.toString()
            ratingTextView.text = game.gameEntity.rating.toString()
            releasedInTextViw.text = game.gameEntity.released.toString()
            metacriticTextView.text = game.gameEntity.metacritic.toString()
            twitchCountTextView.text = game.gameDetails?.gameDetailsEntity?.twitchCount.toString()
            redditCountTextView.text = game.gameDetails?.gameDetailsEntity?.redditCount.toString()
            youtubeCountTextView.text = game.gameDetails?.gameDetailsEntity?.youtubeCount.toString()
            descriptionTextView.text = game.gameDetails?.gameDetailsEntity?.description
            showMoreButton.setOnClickListener {
                descriptionMaxLines = if (descriptionMaxLines == DESCRIPTION_MAX_LINES) {
                    showMoreButton.text = resources.getString(R.string.showLess)
                    Int.MAX_VALUE
                } else {
                    showMoreButton.text = resources.getString(R.string.showMore)
                    DESCRIPTION_MAX_LINES
                }
                descriptionTextView.maxLines = descriptionMaxLines
            }
            yetStatusCount.text = game.addedByStatus?.yet.toString()
            ownedStatusCount.text = game.addedByStatus?.owned.toString()
            beatenStatusCount.text = game.addedByStatus?.beaten.toString()
            toPlayStatusCount.text = game.addedByStatus?.toplay.toString()
            playingStatusCount.text = game.addedByStatus?.playing.toString()
            droppeStatusdCount.text = game.addedByStatus?.dropped.toString()

            ratingTop.text = game.gameEntity.ratingTop.toString()
            addedCount.text = game.gameEntity.added.toString()
            reviewsCount.text = game.gameDetails?.gameDetailsEntity?.reviewsCount.toString()
            playtime.text = game.gameDetails?.gameDetailsEntity?.playtime.toString() + HOURS
            textReviewsCount.text = game.gameDetails?.gameDetailsEntity?.reviewsTextCount.toString()
            achievementsCount.text =
                game.gameDetails?.gameDetailsEntity?.achievementsCount.toString()
            game.gameDetails?.gameDetailsEntity?.website?.let {
                setGoToButtonVisibility(goToWebsiteButton, it)
            }
            game.gameDetails?.gameDetailsEntity?.redditUrl?.let {
                setGoToButtonVisibility(goToRedditButton, it)
            }
            game.gameDetails?.gameDetailsEntity?.metacriticUrl?.let {
                setGoToButtonVisibility(goToMetacriticButton, it)
            }
            toolBar.setNavigationOnClickListener { requireActivity().onBackPressed() }
            setRequirements(game)
            showRatings(game)
            showFilters(game)
            placeholdersContainer.prepareFadeOutAnimation {
                placeholdersContainer.visibility = View.GONE
            }.start()
            animateFirstVisit(prepareContentViews())
        }
    }

    private fun setGoToButtonVisibility(button: Button, url: String) {
        if(isEmptyField(url)) return
        button.visibility = View.VISIBLE
        button.setOnClickListener { onGoToButtonClick(url) }
    }

    private fun onGoToButtonClick(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun setRequirements(game: Game) {
        game.platforms?.find {
            it.slug == PC_SLUG
        }.let {
            if (it == null) {
                binding!!.requirementsContainer.visibility = View.GONE
                return@let
            }
            it.requirementsMinimum?.let { rec -> binding!!.requirementsMin.text = rec }
            it.requirementsRecommended?.let { rec -> binding!!.requirementsRec.text = rec }
        }
    }

    private fun setLikeColorFilters(likeIcon: ImageView, isLiked: Boolean) {
        if (isLiked) likeIcon.setIconColorFilter(requireContext(), com.example.core.R.color.rad)
        else likeIcon.setIconColorFilter(requireContext(), com.example.core.R.color.grayLite)
    }

    private fun showFilters(game: Game) {
        with(binding!!) {
            showFilters(
                tagsContainer,
                tagsRecyclerView,
                tagsAdapter,
                viewModel.getFilters(game.gameDetails?.tags)
            )
            showFilters(
                storesContainer,
                storesRecyclerView,
                storesAdapter,
                viewModel.getFilters(game.gameDetails?.stores?.mapNotNull { it.store })
            )
            showFilters(
                genresContainer,
                genresRecyclerView,
                genresAdapter,
                viewModel.getFilters(game.gameDetails?.genres)
            )
            showFilters(
                developersContainer,
                developersRecyclerView,
                developersAdapter,
                viewModel.getFilters(game.gameDetails?.developers)
            )
            showFilters(
                publisherContainer,
                publishersRecyclerView,
                publishersAdapter,
                viewModel.getFilters(game.gameDetails?.publishers)
            )
            showFilters(
                releasedInContainer,
                releasedInRecyclerView,
                platformsAdapter,
                viewModel.getFilters(game.platforms)
            )
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
                val width =
                    ratingLayout.progress.width / (largestValue / if (rating.count == 0) 1 else rating.count)
                ratingLayout.progress.layoutParams = LinearLayout.LayoutParams(
                    width.toInt(), ratingLayout.progress.height
                )
                ratingLayout.root.prepareFadeInAnimation().start()
            }
            binding!!.progressContainer.addView(ratingLayout.root)
        }
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(
            BitmapFactory.decodeResource(
                resources,
                com.example.core.R.drawable.ic_screenshot_placeholder
            )
        )
    )

    private fun preparePlaceholderViews(): List<View> = listOf(
        binding!!.appBarPlaceholder,
        binding!!.viewPagerPlaceholder,
        binding!!.ratingsPlaceholder,
        binding!!.progressPlaceholders,
        binding!!.releasedInPlaceholder,
        binding!!.aboutPlaceholder,
        binding!!.addedByStatusPlaceholder,
        binding!!.genresPlaceholder,
        binding!!.tagsPlaceholder,
    )

    private fun prepareContentViews(): List<View> = listOf(
        binding!!.detailsContainer,
        binding!!.appBar,
        binding!!.screenshotViewPager,
        binding!!.ratingsContainer,
        binding!!.progressContainer,
        binding!!.releasedInContainer,
        binding!!.aboutContainer,
        binding!!.addedByStatusContainer,
        binding!!.genresContainer,
        binding!!.tagsContainer,
    )

}