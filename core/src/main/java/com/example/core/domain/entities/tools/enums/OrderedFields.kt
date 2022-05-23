package com.example.core.domain.entities.tools.enums

import com.example.core.domain.entities.filters.BaseFilter
import com.example.core.domain.entities.tools.constants.StringConstants.DASH_SIGN

enum class OrderedFields(val orderingField: String) {
    NAME(RequestParams.NAME.slug),
    RELEASED(RequestParams.RELEASED.slug),
    ADDED(RequestParams.ADDED.slug),
    CREATED(RequestParams.CREATED.slug),
    UPDATED(RequestParams.UPDATED.slug),
    RATING(RequestParams.RATING.slug),
    METACRITIC(RequestParams.METACRITIC.slug)
}

fun getOrderedFields(): List<BaseFilter> {
    val orderedFiledTitle = RequestParams.ORDERING.myName
    return OrderedFields.values().flatMap {
        listOf(
            BaseFilter(
                id = it.orderingField,
                name = it.orderingField,
                categoryName = orderedFiledTitle,
                slug = it.orderingField,
            ),
            BaseFilter(
                id = it.reverseOrder,
                name = DASH_SIGN + it.orderingField,
                categoryName = orderedFiledTitle,
                slug = it.orderingField,
            )
        )
    }
}

val OrderedFields.reverseOrder: String
    get() = DASH_SIGN + this.orderingField

