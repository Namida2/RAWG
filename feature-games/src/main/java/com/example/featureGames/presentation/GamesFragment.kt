package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.R
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.games.Game
import com.example.core.domain.interfaces.OnNewGetRequestCallback
import com.example.core.domain.entities.tools.constants.Constants.DEFAULT_SPAN_COUNT
import com.example.core.domain.entities.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.entities.tools.extensions.createMessageAlertDialog
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewAdapter
import com.example.featureGameDetails.presentation.GameDetailsFragment
import com.example.featureGameDetails.presentation.GameDetailsFragment.Companion.GAME_ID_TAG
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.domain.di.GamesDepsStore
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.*
import com.example.featureGames.presentation.recyclerView.itemDecorations.GamesItemDecorations
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlin.properties.Delegates

class GamesFragment : Fragment(), GamesAdapterDelegateCallback {
    private var smallMargin by Delegates.notNull<Int>()
    private var largeMargin by Delegates.notNull<Int>()
    private var topMargin by Delegates.notNull<Int>()
    private var binding: FragmentGamesBinding? = null

    private lateinit var viewModel: GamesViewModel
    private lateinit var screenTag: GameScreenTags
    private lateinit var adapter: BaseRecyclerViewAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        screenTag = arguments?.get(
            hashCode().toString()
        ) as? GameScreenTags ?: throw IllegalArgumentException()
        logE("onAttach, screenTag: $screenTag")
        smallMargin = resources.getDimensionPixelSize(R.dimen.small_margin)
        largeMargin = resources.getDimensionPixelSize(R.dimen.large_margin)
        topMargin = resources.getDimensionPixelSize(R.dimen.games_top_margin)
        viewModel = ViewModelProvider(
            this, ViewModelFactory(screenTag)
        )[GamesViewModel::class.java].also { it.getGames() }
        adapter = BaseRecyclerViewAdapter(
            listOf(
                GamesAdapterDelegate(this),
                GamesPlaceholderDelegate(),
                GameErrorPageAdapterDelegate(viewModel)
            )
        )
    }

    override fun onResume() {
        logE("onResume: $screenTag")
        super.onResume()
    }

    override fun onPause() {
        logE("onPause: $screenTag")
        super.onPause()
    }

    override fun onStop() {
        logE("onStop: $screenTag")
        super.onStop()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentGamesBinding.inflate(inflater, container, false).let {
        binding = it; it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        logE("recyclerView: ${binding!!.gamesRecyclerView.hashCode()}")
        with(binding!!) {
            gamesRecyclerView.layoutManager =
                StaggeredGridLayoutManager(DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
            gamesRecyclerView.adapter = adapter
            gamesRecyclerView.addItemDecoration(
                GamesItemDecorations(
                    topMargin, largeMargin, smallMargin
                )
            )
            RecyclerViewScrollListener(gamesRecyclerView, viewModel)
        }
        observeSingleEventsEvent()
        observeOnStateChangedEvent()
    }

    fun getOnNewRequestCallback(): OnNewGetRequestCallback<GamesGetRequest> = viewModel.also {
        logD(screenTag.toString())
    }

    private fun observeSingleEventsEvent() {
        viewModel.singleEvents.observe(viewLifecycleOwner) {
//            logD(screenTag.toString())
//            logE("$this: observeSingleEventsEvent, screenTag: $screenTag")
            when (it) {
                is GamesVMSingleEvents.NewGamesEvent -> {
                    // TODO: Submit list after debounce
                    // TODO: Add a tabsLayout, filters and search bar //STOPPED//
                    adapter.submitList(it.event.getData() ?: return@observe)
                }
                is GamesVMSingleEvents.NetworkConnectionLostEvent -> {
                    it.event.getData() ?: return@observe
                    requireContext().createMessageAlertDialog(checkNetworkConnectionMessage)
                        ?.show(parentFragmentManager, "")
                }
            }
        }
    }

    private fun observeOnStateChangedEvent() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is GamesVMStats.AllGamesFromRequestHaveBeenLoaded -> {
                    if (viewModel.snackBarIsShowing) return@observe
                    showSnackBar(R.string.pageNotFoundMessage)
                }
                is GamesVMStats.Error -> {
                    requireContext().createMessageAlertDialog(state.message)
                        ?.show(parentFragmentManager, "")
                }
                is GamesVMStats.Default -> {}
            }
        }
    }

    private fun showSnackBar(messageStringId: Int) {
        viewModel.snackBarIsShowing = true
        Snackbar.make(binding!!.root, messageStringId, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
            .addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    viewModel.snackBarIsShowing = false
                }
            }).setTextColor(ContextCompat.getColor(requireContext(), R.color.white)).show()
    }

    override fun onGameClick(clickedGameInfo: ClickedGameInfo) {
        GamesDepsStore.navigationCallback?.navigateTo(
            GameDetailsFragment().also {
                it.arguments = bundleOf(GAME_ID_TAG to clickedGameInfo.gameId)
            },
            clickedGameInfo.gameRootView,
            resources.getString(R.string.defaultEndTransitionName)
        )
    }

    override fun onGameLikeButtonClick(game: Game) {
        viewModel.onLikeButtonClick(game)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
