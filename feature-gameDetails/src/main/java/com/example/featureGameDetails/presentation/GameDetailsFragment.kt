package com.example.featureGameDetails.presentation

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.showIfNotAdded
import com.example.core.presentaton.dialogs.ClosedQuestionDialog
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
import kotlin.properties.Delegates

// TODO: Complete the GameDetailsScreen, implement the likes of games and saving them to the local storage //STOPPED//
class GameDetailsFragment : BaseFragment() {
    private var defaultScale by Delegates.notNull<Float>()
    private var currentPageMargin by Delegates.notNull<Int>()
    private var pageLargeMargin by Delegates.notNull<Int>()
    private var pageSmallMargin by Delegates.notNull<Int>()
    private var closedQuestionDialog = ClosedQuestionDialog<Unit>(
        onNegative = {

        }, onPositive = {

        })
    private var gameId by Delegates.notNull<Int>()

    private var binding: FragmentGameDetailsBinding? = null
    private lateinit var viewModel: GameDetailsViewModel
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
        initViewPager()
        postponeEnterTransition()
        observeViewModelEvents()
        observeViewModelStates()
        view.doOnPreDraw { startPostponedEnterTransition(); animateFirstVisit() }
        binding!!.ratingsContainer.setOnClickListener {
            animateFirstVisit()
        }
    }

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
            springStartPosition = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition)
                .toFloat(),
            delayBetweenAnimations = resources.getInteger(com.example.core.R.integer.smallAnimationDelay)
                .toLong(),
            startDelay = resources.getInteger(com.example.core.R.integer.smallSpringStartPosition)
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
    private fun iniViews(game: Game) {
        with(binding!!) {
            metacriticTextView.text = game.metacritic.toString()
            ratingTextView.text = game.rating.toString()
            twitchCountTextView.text = game.gameDetails?.twitchCount.toString()
            redditCountTextView.text = game.gameDetails?.redditCount.toString()
            youtubeCountTextView.text = game.gameDetails?.youtubeCount.toString()

            releasedAtTextViw.text = game.released.toString()
            descriptionTextView.text = Html.fromHtml(
                game.gameDetails?.description, Html.FROM_HTML_MODE_LEGACY
            ).toString().trim()

        }
    }

    private fun getPostImagesList(): List<GameScreenshot> = listOf(
        GameScreenshot(BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),
        GameScreenshot(BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),
        GameScreenshot(BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),
        GameScreenshot(BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),
        GameScreenshot(BitmapFactory.decodeResource(resources, R.drawable.im_game_test)),
    )

}