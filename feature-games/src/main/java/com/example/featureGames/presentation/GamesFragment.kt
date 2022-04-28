package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.tools.constants.Constants.GAMES_SPAN_COUNT
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.core.domain.tools.enums.GameScreens
import com.example.core.R
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.GamesDelegate
import com.example.featureGames.presentation.recyclerView.delegates.GamesPlaceholderDelegate
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class GamesFragment : Fragment() {
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            GamesDelegate(),
            GamesPlaceholderDelegate()
        )
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel =
            ViewModelFactory(GameScreens.TOP_PICKS).create(GamesViewModel::class.java).also {
                it.readGames()
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentGamesBinding.inflate(inflater, container, false).let {
        binding = it; it.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        with(binding) {
            gamesRecyclerView.layoutManager =
                StaggeredGridLayoutManager(
                    GAMES_SPAN_COUNT,
                    StaggeredGridLayoutManager.VERTICAL
                ).also {
                    it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            gamesRecyclerView.adapter = adapter
            RecyclerViewScrollListener(gamesRecyclerView, viewModel)
        }
        observeSingleEventsEvent()
        observeOnStateChangedEvent()
    }

    private fun observeSingleEventsEvent() {
        viewModel.singleEvents.observe(viewLifecycleOwner) {
            when(it) {
                is GamesVMSingleEvents.NewGamesEvent ->
                    adapter.submitList(it.event.getData() ?: return@observe)
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
                    if(viewModel.snackBarIsShowing) return@observe
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
        Snackbar.make(binding.root, messageStringId, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.black))
            .addCallback(object: BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    viewModel.snackBarIsShowing = false
                }
            })
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.white)).show()
    }
}