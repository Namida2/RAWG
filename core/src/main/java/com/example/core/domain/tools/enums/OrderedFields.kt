package com.example.core.domain.tools.enums

import com.example.core.domain.entities.filters.Filter
import com.example.core.domain.tools.constants.StringConstants.DASH_SIGN

enum class OrderedFields(val orderingField: String) {
    NAME(RequestParams.NAME.slug),
    RELEASED(RequestParams.RELEASED.slug),
    ADDED(RequestParams.ADDED.slug),
    CREATED(RequestParams.CREATED.slug),
    UPDATED(RequestParams.UPDATED.slug),
    RATING(RequestParams.RATING.slug),
    METACRITIC(RequestParams.METACRITIC.slug)
}

fun getOrderedFields(): List<Filter> {
    val orderedFiledTitle = RequestParams.ORDERING.myName
    return OrderedFields.values().flatMap {
        listOf(
            Filter(
                orderedFiledTitle,
                it.orderingField,
                it.orderingField
            ),
            Filter(
                orderedFiledTitle,
                DASH_SIGN + it.orderingField,
                it.reverseOrder
            )
        )
    }
}

val OrderedFields.reverseOrder: String
    get() = DASH_SIGN + this.orderingField

