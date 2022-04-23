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
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.domain.di.modules.UseCasesImpNames.ALL_GAMES
import com.example.featureGames.domain.model.GamePlaceHolder
import com.example.featureGames.presentation.recyclerView.RecyclerViewScrollListener
import com.example.featureGames.presentation.recyclerView.delegates.GamesDelegate
import com.example.featureGames.presentation.recyclerView.delegates.GamesPlaceholderDelegate

class GamesFragment : Fragment() {
    private val placeholdersCount = 20
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
        viewModel = ViewModelFactory(ALL_GAMES).create(GamesViewModel::class.java).also {
            it.screenTag = ALL_GAMES
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
                StaggeredGridLayoutManager(GAMES_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL).also {
                    it.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            gamesRecyclerView.adapter = adapter.also { it.submitList(getPlaceholders()) }
            RecyclerViewScrollListener(gamesRecyclerView, viewModel)
        }
        observeNewGamesEvent()
    }

    private fun observeNewGamesEvent() {
        viewModel.newGamesEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { games ->
                logD("fragment: submitList")
                adapter.submitList(games)
            }
        }
    }

    private fun getPlaceholders(): List<GamePlaceHolder> {
        val placeHolder = GamePlaceHolder()
        val result = mutableListOf<GamePlaceHolder>()
        return repeat(placeholdersCount) {
            result.add(placeHolder)
        }.run { result }

    }

}