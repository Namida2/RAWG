package com.example.featureGames.domain.tools

import com.example.core.domain.tools.constants.RequestParams

private const val reverseOrderSign = "-"
enum class OrderedFields(val orderingField: String) {
    NAME(RequestParams.NAME.slug),
    RELEASED(RequestParams.RELEASED.slug),
    ADDED(RequestParams.ADDED.slug),
    CREATED(RequestParams.CREATED.slug),
    UPDATED(RequestParams.UPDATED.slug),
    RATING(RequestParams.RATING.slug),
    METACRITIC(RequestParams.METACRITIC.slug)
}

val OrderedFields.reverseOrder: String
get() = reverseOrderSign + this.orderingField

