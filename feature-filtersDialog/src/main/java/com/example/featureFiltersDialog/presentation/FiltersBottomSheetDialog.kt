package com.example.featureFiltersDialog.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
    private val adapter = BaseRecyclerViewAdapter(
        listOf(
            FilterCategoryNameAdapterDelegate(),
            FiltersContainerAdapterDelegate()
        )
    )

    // TODO: Add itemDecorations and making requests after setting filters //STOPPED//
    companion object {
        private val isExists = AtomicBoolean(false)
        fun getNewInstance(): FiltersBottomSheetDialog? =
            (if (!isExists.get()) FiltersBottomSheetDialog() else null).also { isExists.set(true) }
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
            adapter.submitList(viewModel.getFilters())
            filtersContainerRecyclerView.adapter = adapter
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