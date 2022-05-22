package com.example.featureFiltersDialog.presentation

import android.text.Editable
import android.text.TextWatcher
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.entities.tools.SingleEvent
import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.entities.filters.FilterCategory
import com.example.core.domain.entities.filters.FilterCategoryName
import com.example.core.domain.entities.filters.FiltersHolder
import com.example.core.domain.entities.requests.GamesGetRequest
import com.example.core.domain.entities.tools.constants.Constants.MAX_METACRITIC
import com.example.core.domain.entities.tools.constants.Constants.MIN_METACRITIC
import com.example.core.domain.entities.tools.constants.StringConstants.DEFAULT_METACRITIC
import com.example.core.domain.entities.tools.constants.StringConstants.DEFAULT_START_DATE
import com.example.core.domain.entities.tools.constants.StringConstants.FILTER_CATEGORY_NOT_FOUND
import com.example.core.domain.entities.tools.constants.StringConstants.DASH_SIGN
import com.example.core.domain.entities.tools.constants.StringConstants.EMPTY_STRING
import com.example.core.domain.entities.tools.enums.RequestParams
import com.example.core.domain.entities.tools.enums.getOrderedFields
import com.example.core.domain.entities.tools.extensions.isEmptyField
import com.example.core.domain.entities.tools.extensions.logD
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType
import com.example.featureFiltersDialog.domain.di.FiltersDepsStore.onNewRequestCallback
import com.example.featureFiltersDialog.domain.entities.toDateString
import com.example.featureFiltersDialog.presentation.recyclerView.FiltersContainerAdapterDelegateCallback
import java.util.*
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

    lateinit var minMetacriticLastSaved: String
    lateinit var maxMetacriticLastSaved: String
    lateinit var startDateLastSaved: String
    lateinit var endDateLastSaved: String
    private val _events = MutableLiveData<FilterVMEvents<Any>>()
    private var currentFilterItems = mutableListOf<BaseRecyclerViewType>()
    val events: LiveData<FilterVMEvents<Any>> = _events

    init { resetStringValues() }

    fun getFilters() {
        if (currentFilterItems.isEmpty())
            onNewFilterItemsEvent(prepareFiltersForView())
        else onNewFilterItemsEvent(currentFilterItems)
    }

    fun onAcceptButtonClick(search: String) {
        val name = if(isEmptyField(search)) null else search
        val builder = GamesGetRequest.Builder()
        name?.let { builder.setName(it) }
        builder.setMetacritic(
            if (minMetacriticLastSaved == EMPTY_STRING)
                MIN_METACRITIC.toString() else minMetacriticLastSaved,
            if (maxMetacriticLastSaved == EMPTY_STRING)
                MAX_METACRITIC.toString() else maxMetacriticLastSaved,
        )
        builder.setDates(startDateLastSaved, endDateLastSaved)
        currentFilterItems.forEach { filterCategory ->
            if (filterCategory is FilterCategory) {
                val filters = filterCategory.getSelectedFilters().map { it.id }.toTypedArray()
                if (filters.isEmpty()) return@forEach
                when (filterCategory.categoryName) {
                    RequestParams.ORDERING.myName -> builder.addOrdering(filters.first())
                    RequestParams.DEVELOPERS.myName -> builder.addDevelopers(*filters)
                    RequestParams.GENRES.myName -> builder.addGenres(*filters)
                    RequestParams.PLATFORMS.myName -> builder.addPlatform(*filters)
                    RequestParams.PUBLISHERS.myName -> builder.addPublishers(*filters)
                    RequestParams.STORES.myName -> builder.addStores(*filters)
                    RequestParams.TAGS.myName -> builder.addTags(*filters)
                }
            }
        }
        onNewRequestCallback?.onNewRequest(builder.build().also {
            logD(it.getParams().toString())
        })
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
                                Filter(filter.categoryName, filter.name, filter.id)
                            )
                        }; newFilters
                    }
                )
            )
        }
        filtersHolder.filters = newList.toMutableList()
        resetStringValues()
        prepareFiltersForView()
        _events.value = FilterVMEvents.OnFilterClearedEvent(
            SingleEvent(currentFilterItems)
        )
    }

    val metacriticMinTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onMetacriticValueChanged(s, FilterVMEvents.OnMinMetacriticWrongValue::class) {
                minMetacriticLastSaved = it
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }

    val metacriticMaxTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onMetacriticValueChanged(s, FilterVMEvents.OnMaxMetacriticWrongValue::class) {
                maxMetacriticLastSaved = it
            }
        }

        override fun afterTextChanged(s: Editable?) = Unit
    }

    private fun onMetacriticValueChanged(
        sequence: CharSequence?,
        onWringValueEvent: KClass<out FilterVMEvents<Any>>,
        onNewCorrectValue: (newValue: String) -> Unit
    ) {
        val newString = sequence.toString()
        if (newString.isDigitsOnly() && newString.isNotEmpty()) {
            val newValue = newString.toInt()
            if (newValue > MAX_METACRITIC) emitValueForMetacriticFields(
                minMetacriticLastSaved, onWringValueEvent
            )
            else onNewCorrectValue.invoke(newString)
        } else if (!newString.isDigitsOnly())
            emitValueForMetacriticFields(DEFAULT_METACRITIC, onWringValueEvent)
    }

    private fun emitValueForMetacriticFields(
        value: String,
        onWrongValueEvent: KClass<out FilterVMEvents<Any>>
    ) = when (onWrongValueEvent) {
        FilterVMEvents.OnMinMetacriticWrongValue::class -> {
            minMetacriticLastSaved = value
            _events.value = FilterVMEvents.OnMinMetacriticWrongValue(SingleEvent(value))
        }
        FilterVMEvents.OnMaxMetacriticWrongValue::class -> {
            maxMetacriticLastSaved = value
            _events.value = FilterVMEvents.OnMaxMetacriticWrongValue(SingleEvent(value))
        }
        else -> throw IllegalArgumentException()
    }


    private fun prepareFiltersForView(): List<BaseRecyclerViewType> {
        filtersHolder.filters.flatMap {
            listOf<BaseRecyclerViewType>(FilterCategoryName(it.categoryName)) + it
        }.also {
            currentFilterItems =
                (listOf(
                    FilterCategoryName(RequestParams.ORDERING.myName)
                ) + FilterCategory(
                    RequestParams.ORDERING.myName,
                    getOrderedFields().toMutableList(),
                    true
                ) + it).toMutableList()
        }
        return currentFilterItems
    }


    override fun onNewFilter(filterCategory: FilterCategory) {
        logD(filterCategory.categoryName)
        currentFilterItems.indexOfFirst {
            it is FilterCategory && it.categoryName == filterCategory.categoryName
        }.also { position ->
            if (position == -1) throw IllegalArgumentException(FILTER_CATEGORY_NOT_FOUND + filterCategory)
            currentFilterItems[position] = filterCategory
            //Update the parent recyclerView to make the clearButton work correctly
            onNewFilterItemsEvent(currentFilterItems)
        }
    }

    private fun onNewFilterItemsEvent(filterItems: List<BaseRecyclerViewType>) {
        _events.value = FilterVMEvents.OnNewFilterItemsEvent(
            SingleEvent(filterItems)
        )
    }

    private fun resetStringValues() {
        val today = Calendar.getInstance().timeInMillis.toDateString(DASH_SIGN)
        startDateLastSaved = DEFAULT_START_DATE
        endDateLastSaved = today
        minMetacriticLastSaved = EMPTY_STRING
        maxMetacriticLastSaved = EMPTY_STRING
    }

}