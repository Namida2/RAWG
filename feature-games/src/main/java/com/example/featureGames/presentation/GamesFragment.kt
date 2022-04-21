package com.example.featureGames.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.databinding.FragmentGamesBinding
import com.example.featureGames.domain.ViewModelFactory
import com.example.featureGames.domain.di.modules.UseCasesImpNames.ALL_GAMES
import com.example.featureGames.domain.model.Game
import com.example.featureGames.presentation.delegates.GamesDelegate

class GamesFragment : Fragment() {
    private val spanCount = 2
    private lateinit var binding: FragmentGamesBinding
    private lateinit var viewModel: GamesViewModel
    private val adapter = BaseRecyclerViewAdapter(
        listOf(GamesDelegate())
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = ViewModelFactory(ALL_GAMES).create(GamesViewModel::class.java).also {
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
            gamesContainerRecyclerView.layoutManager =
                GridLayoutManager(requireContext(), spanCount)
            gamesContainerRecyclerView.adapter = adapter.also {
                it.submitList(
                    listOf(
                        Game(0), Game(1),
                        Game(2), Game(3),
                        Game(4), Game(5),
                        Game(6), Game(7),
                        Game(8), Game(9),
                        Game(10), Game(11),
                    )
                )
            }
        }
        observeNewGamesEvent()
    }

    private fun observeNewGamesEvent() {
        viewModel.newGamesEvent.observe(viewLifecycleOwner) {
            it.getData()?.let { games ->
                adapter.submitList(games)
            }
        }
    }

}