package com.example.featureFiltersDialog.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.tools.constants.StringConstants.DEFAULT_DATE
import com.example.core.domain.tools.constants.StringConstants.DEFAULT_METACRITIC
import com.example.core.domain.tools.constants.StringConstants.EMPTY_STRING
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureFiltersDialog.databinding.DialogFiltersBinding
import com.example.featureFiltersDialog.domain.ViewModelFactory
import com.example.featureFiltersDialog.domain.entities.toDateString
import com.example.featureFiltersDialog.presentation.recyclerView.FilterCategoryNameAdapterDelegate
import com.example.featureFiltersDialog.presentation.recyclerView.FiltersContainerAdapterDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
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
            setListeners()
        }
        observeViewModelEvents()
    }

    private fun setListeners() {
        setOnDateClickListener(binding!!.releaseStartDateTextView)
        setOnDateClickListener(binding!!.releaseEndDateTextView)
        binding!!.metacriticMinTexView.addTextChangedListener(viewModel.metacriticMinTextWatcher)
        binding!!.metacriticMaxTexView.addTextChangedListener(viewModel.metacriticMaxTextWatcher)
    }

    private fun setOnDateClickListener(field: TextView) {
        field.setOnClickListener {
            MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build().also {
                it.addOnPositiveButtonClickListener { dateLong ->
                    field.text = dateLong.toDateString()
                }
            }.show(parentFragmentManager, "")
        }
    }

    private fun observeViewModelEvents() {
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is FilterVMEvents.OnNewFilterItemsEvent -> {
                    it.event.getData()?.let { filterItems ->
                        adapter.submitList(filterItems)
                    }
                }
                is FilterVMEvents.OnMinMetacriticWrongValue -> {
                    onNewMetacriticValue(it.event.getData(), binding!!.metacriticMinTexView)
                }
                is FilterVMEvents.OnMaxMetacriticWrongValue -> {
                    onNewMetacriticValue(it.event.getData(), binding!!.metacriticMaxTexView)
                }
                is FilterVMEvents.OnFilterClearedEvent -> {
                    it.event.getData()?.let { filterItems ->
                        adapter.submitList(filterItems)
                        with(binding!!) {
                            searchEditText.setText(EMPTY_STRING)
                            metacriticMinTexView.setText(EMPTY_STRING)
                            metacriticMaxTexView.setText(EMPTY_STRING)
                            releaseStartDateTextView.text = DEFAULT_DATE
                            releaseEndDateTextView.text = DEFAULT_DATE
                        }
                    }
                }
            }
        }
    }

    private fun onNewMetacriticValue(value: String?, field: EditText) {
        value?.let { collectValue ->
            field.setText(collectValue)
            field.setSelection(value.length)
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