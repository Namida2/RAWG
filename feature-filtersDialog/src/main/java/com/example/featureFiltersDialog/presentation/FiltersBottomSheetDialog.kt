package com.example.featureFiltersDialog.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.domain.entities.tools.constants.StringConstants.DEFAULT_START_DATE
import com.example.core.domain.entities.tools.constants.StringConstants.EMPTY_STRING
import com.example.core.domain.entities.tools.extensions.startEnterSpringAnimation
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewAdapter
import com.example.featureFiltersDialog.databinding.DialogFiltersBinding
import com.example.featureFiltersDialog.domain.ViewModelFactory
import com.example.featureFiltersDialog.domain.entities.toDateString
import com.example.featureFiltersDialog.presentation.recyclerView.FilterCategoryNameAdapterDelegate
import com.example.featureFiltersDialog.presentation.recyclerView.FiltersCategoryAdapterDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class FiltersBottomSheetDialog : BottomSheetDialogFragment() {
    private var binding: DialogFiltersBinding? = null
    private val viewModel by activityViewModels<FiltersViewModel> { ViewModelFactory }
    private lateinit var adapter: BaseRecyclerViewAdapter

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
                FiltersCategoryAdapterDelegate(viewModel)
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
            filtersContainerRecyclerView.setHasFixedSize(true)
            filtersContainerRecyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            viewModel.getFilters()
            applyButton.setOnClickListener {
                viewModel.onAcceptButtonClick(searchEditText.text.toString())
                this@FiltersBottomSheetDialog.dismiss()
            }
            clearButton.setOnClickListener { viewModel.onClearButtonClick() }
            releaseStartDateTextView.text = viewModel.startDateLastSaved
            releaseEndDateTextView.text = viewModel.endDateLastSaved
            metacriticMinTexView.setText(viewModel.minMetacriticLastSaved)
            metacriticMaxTexView.setText(viewModel.maxMetacriticLastSaved)
            searchEditText.setText(viewModel.search)
            setListeners()
        }
        observeViewModelEvents()
    }

    private fun setListeners() {
        setOnDateClickListener(binding!!.releaseStartDateTextView) { viewModel.startDateLastSaved = it }
        setOnDateClickListener(binding!!.releaseEndDateTextView) { viewModel.endDateLastSaved = it }
        binding!!.metacriticMinTexView.addTextChangedListener(viewModel.metacriticMinTextWatcher)
        binding!!.metacriticMaxTexView.addTextChangedListener(viewModel.metacriticMaxTextWatcher)
    }

    private fun setOnDateClickListener(field: TextView, onNewDate: (newDate: String) -> Unit) {
        field.setOnClickListener {
            MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build().also {
                    it.addOnPositiveButtonClickListener { dateLong ->
                        field.text = dateLong.toDateString().also(onNewDate)
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
                        val today = Calendar.getInstance().timeInMillis.toDateString()
                        adapter.submitList(filterItems)
                        with(binding!!) {
                            searchEditText.setText(EMPTY_STRING)
                            metacriticMinTexView.setText(EMPTY_STRING)
                            metacriticMaxTexView.setText(EMPTY_STRING)
                            releaseStartDateTextView.text = DEFAULT_START_DATE
                            releaseEndDateTextView.text = today
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