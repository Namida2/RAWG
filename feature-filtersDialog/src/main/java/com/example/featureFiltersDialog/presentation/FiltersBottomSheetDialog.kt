package com.example.featureFiltersDialog.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureFiltersDialog.databinding.DialogFiltersBinding
import com.example.featureFiltersDialog.domain.ViewModelFactory
import com.example.featureFiltersDialog.presentation.recyclerView.FilterCategoryNameAdapterDelegate
import com.example.featureFiltersDialog.presentation.recyclerView.FiltersContainerAdapterDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.concurrent.atomic.AtomicBoolean

class FiltersBottomSheetDialog : BottomSheetDialogFragment() {
    private var binding: DialogFiltersBinding? = null
    private val viewModel by viewModels<FiltersViewModel> { ViewModelFactory }
    private lateinit var adapter: BaseRecyclerViewAdapter

    // TODO: Add making requests after setting filters, game detailScreen and implement the myLikes //STOPPED//
    companion object {
        private val isExists = AtomicBoolean(false)
        fun getNewInstance(): FiltersBottomSheetDialog? =
            (if (!isExists.get()) FiltersBottomSheetDialog() else null).also { isExists.set(true) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        adapter = BaseRecyclerViewAdapter(
            listOf(
                FilterCategoryNameAdapterDelegate(),
                FiltersContainerAdapterDelegate(viewModel)
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = DialogFiltersBinding.inflate(inflater, container, false).run {
        binding = this; this.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding!!) {
            filtersContainerRecyclerView.adapter = adapter
            filtersContainerRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewModel.getFilters()
            acceptButton.setOnClickListener { viewModel.onAcceptButtonClick(adapter.currentList) }
            clearButton.setOnClickListener { viewModel.onClearButtonClick() }
        }
        observeViewModelEvents()
    }

    private fun observeViewModelEvents() {
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is FilterVMEvents.OnNewFilterItemsEvent -> {
                    it.event.getData()?.let { filterItems ->
                        val current = adapter.currentList
                        if (current == filterItems)
                            logD("same")
                        else logD("new")
                        adapter.submitList(filterItems)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        isExists.set(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}