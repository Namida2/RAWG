package com.example.featureGames.domain.tools

import com.example.core.domain.tools.constants.RequestParams

private const val reverseOrderSign = "-"
enum class OrderedFields(val orderingField: String) {
    NAME(RequestParams.NAME),
    RELEASED(RequestParams.RELEASED),
    ADDED(RequestParams.ADDED),
    CREATED(RequestParams.CREATED),
    UPDATED(RequestParams.UPDATED),
    RATING(RequestParams.RATING),
    METACRITIC(RequestParams.METACRITIC)
}

val OrderedFields.reverseOrder: String
get() = reverseOrderSign + this.orderingField

