package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.tools.Constants.GAMES_SPAN_COUNT
import com.example.core.domain.tools.Constants.MIN_ITEMS_COUNT_FOR_NEXT_PAGE
import com.example.core.domain.tools.Constants.PAGE_SIZE
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.domain.model.GamePlaceHolder
import com.example.featureGames.domain.tools.GameScreens
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.GamesDelegate
import com.example.featureGames.presentation.recyclerView.delegates.GamesPlaceholderDelegate
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

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
            gamesRecyclerView.adapter = adapter.also { it.submitList(viewModel.getPlaceholders()) }
            RecyclerViewScrollListener(gamesRecyclerView, viewModel)
        }
        observeNewGamesEvent()
        observeLoadingNewPageEvent()
    }

    private fun observeNewGamesEvent() {
        viewModel.newGamesEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { games ->
                adapter.submitList(games)
            }
        }
    }

    private fun observeLoadingNewPageEvent() {
        viewModel.loadingNewPageEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { positions ->
                if (adapter.currentList.size - positions[0] < MIN_ITEMS_COUNT_FOR_NEXT_PAGE) {
                    viewModel.loadNextPage()
                }
            }
        }
    }

}