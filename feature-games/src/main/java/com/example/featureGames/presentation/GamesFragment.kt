package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.tools.constants.Constants.GAMES_SPAN_COUNT
import com.example.core.domain.tools.constants.Messages.checkNetworkConnectionMessage
import com.example.core.domain.tools.extensions.createMessageAlertDialog
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.GamesDelegate
import com.example.featureGames.presentation.recyclerView.delegates.GamesPlaceholderDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        observeNewGamesEvent()
        observeOnStateChangedEvent()
        observeOnNetworkConnectionLostEvent()
    }

    private fun observeNewGamesEvent() {
        viewModel.newGamesEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { games ->
                adapter.submitList(games)
            }
        }
    }

    private fun observeOnStateChangedEvent() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is GamesVMStats.AllGamesFromRequestHaveBeenLoaded -> {

                }
                is GamesVMStats.Default -> {

                }
            }
        }
    }

    private fun observeOnNetworkConnectionLostEvent() {
        viewModel.networkConnectionLostEvent.observe(viewLifecycleOwner) {
            it.getData() ?: return@observe
            requireContext().createMessageAlertDialog(checkNetworkConnectionMessage)
                ?.show(parentFragmentManager, "")
        }
    }

}