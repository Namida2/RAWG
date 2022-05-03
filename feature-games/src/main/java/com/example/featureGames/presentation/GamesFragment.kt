package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.R
import com.example.core.domain.tools.constants.Constants.GAMES_SPAN_COUNT
import com.example.core.domain.tools.constants.Constants.GAME_SCREEN_TAG
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.enums.GameScreenTags
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.GamesDelegate
import com.example.featureGames.presentation.recyclerView.delegates.GamesPlaceholderDelegate
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class GamesFragment : Fragment() {
    private var binding: FragmentGamesBinding? = null
    private lateinit var viewModel: GamesViewModel
    private lateinit var screenTag: GameScreenTags
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            GamesDelegate(),
            GamesPlaceholderDelegate()
        )
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        screenTag = arguments?.get(hashCode().toString()) as? GameScreenTags
            ?: throw IllegalArgumentException()
        logE("onAttach, screenTag: $screenTag")
        viewModel = ViewModelProvider(
            this, ViewModelFactory(screenTag)
        )[GamesViewModel::class.java].also {
//            it.getGames()
        }
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
                StaggeredGridLayoutManager(GAMES_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
            gamesRecyclerView.adapter = adapter
            RecyclerViewScrollListener(gamesRecyclerView, viewModel)
        }
        observeSingleEventsEvent()
        observeOnStateChangedEvent()
    }

    private fun observeSingleEventsEvent() {
        viewModel.singleEvents.observe(viewLifecycleOwner) {
            logD(screenTag.toString())
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
            })
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white)).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}