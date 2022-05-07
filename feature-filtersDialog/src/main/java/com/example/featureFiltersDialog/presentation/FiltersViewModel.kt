package com.example.featureFiltersDialog.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.SingleEvent
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.entities.filters.FilterCategoryName
import com.example.core.domain.entities.filters.FiltersHolder
import com.example.core.domain.tools.constants.StringConstants.DEFAULT_METACRITIC
import com.example.core.domain.tools.constants.StringConstants.FILTER_CATEGORY_NOT_FOUND
import com.example.core.domain.tools.extensions.logD
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType
import com.example.featureFiltersDialog.presentation.recyclerView.FiltersContainerAdapterDelegateCallback
import kotlin.reflect.KClass

sealed interface FilterVMEvents<out T> {
    val event: SingleEvent<out T>

    class OnNewFilterItemsEvent(
        override val event: SingleEvent<List<BaseRecyclerViewType>>
    ) : FilterVMEvents<List<BaseRecyclerViewType>>

    class OnFilterClearedEvent(
        override val event: SingleEvent<List<BaseRecyclerViewType>>
    ) : FilterVMEvents<List<BaseRecyclerViewType>>

    class OnMinMetacriticWrongValue(override val event: SingleEvent<String>) :
        FilterVMEvents<String>

    class OnMaxMetacriticWrongValue(override val event: SingleEvent<String>) :
        FilterVMEvents<String>
}

class FiltersViewModel(
    private val filtersHolder: FiltersHolder
) : ViewModel(), FiltersContainerAdapterDelegateCallback {

    private val metacriticMaxValue = 100
    private var minMetacriticLastSaved = "0"
    private var maxMetacriticLastSaved = "0"
    private val _events = MutableLiveData<FilterVMEvents<Any>>()
    private var currentFilterItems = mutableListOf<BaseRecyclerViewType>()
    val events: LiveData<FilterVMEvents<Any>> = _events

    fun getFilters() {
        if (currentFilterItems.isEmpty())
            onNewFilterItemsEvent(prepareFiltersForView())
        else onNewFilterItemsEvent(currentFilterItems)
    }

    fun onAcceptButtonClick(filterItems: List<BaseRecyclerViewType>) {

    }

    fun onClearButtonClick() {
        val newList = mutableListOf<FilterCategory>()
        filtersHolder.filters.forEach { filterCategory ->
            newList.add(
                filterCategory.copy(
                    filters = run {
                        val newFilters = mutableListOf<Filter>()
                        filterCategory.filters.forEach { filter ->
                            newFilters.add(
                                Filter(filter.categoryName, filter.name, filter.slug)
                            )
                        }; newFilters
                    }
                )
            )
        }
        filtersHolder.filters = newList.toMutableList()
        prepareFiltersForView()
        _events.value = FilterVMEvents.OnFilterClearedEvent(
            SingleEvent(currentFilterItems)
        )
    }

    val metacriticMinTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onMetacriticValueChanged(s, FilterVMEvents.OnMinMetacriticWrongValue::class)
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }

    val metacriticMaxTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onMetacriticValueChanged(s, FilterVMEvents.OnMaxMetacriticWrongValue::class)
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }

    private fun onMetacriticValueChanged(
        sequence: CharSequence?,
        onWringValueEvent: KClass<out FilterVMEvents<Any>>
    ) {
        val newString = sequence.toString()
        if (newString.isDigitsOnly() && newString.isNotEmpty()) {
            val newValue = newString.toInt()
            if (newValue > metacriticMaxValue) {
                minMetacriticLastSaved = metacriticMaxValue.toString()
                emitValueForMetacriticFields(minMetacriticLastSaved, onWringValueEvent)

            }
        } else if(!newString.isDigitsOnly())
            emitValueForMetacriticFields(DEFAULT_METACRITIC, onWringValueEvent)
    }

    private fun emitValueForMetacriticFields(
        value: String,
        onWringValueEvent: KClass<out FilterVMEvents<Any>>
    ) {
        when (onWringValueEvent) {
            FilterVMEvents.OnMinMetacriticWrongValue::class -> {
                _events.value = FilterVMEvents.OnMinMetacriticWrongValue(SingleEvent(value))
            }
            FilterVMEvents.OnMaxMetacriticWrongValue::class -> {
                _events.value = FilterVMEvents.OnMaxMetacriticWrongValue(SingleEvent(value))
            }
            else -> throw IllegalArgumentException()
        }
    }

    private fun prepareFiltersForView(): List<BaseRecyclerViewType> =
        filtersHolder.filters.map {
            listOf<BaseRecyclerViewType>(FilterCategoryName(it.categoryName)) + it
        }.flatten().also {
            currentFilterItems = it.toMutableList()
        }

    override fun onNewFilter(filterCategory: FilterCategory) {
        logD(filterCategory.categoryName)
        currentFilterItems.indexOfFirst {
            it is FilterCategory && it.categoryName == filterCategory.categoryName
        }.also { position ->
            if (position == -1) throw IllegalArgumentException(FILTER_CATEGORY_NOT_FOUND + filterCategory)
            currentFilterItems[position] = filterCategory
            onNewFilterItemsEvent(currentFilterItems)
        }
    }

    private fun onNewFilterItemsEvent(filterItems: List<BaseRecyclerViewType>) {
        _events.value = FilterVMEvents.OnNewFilterItemsEvent(
            SingleEvent(filterItems)
        )
    }

}