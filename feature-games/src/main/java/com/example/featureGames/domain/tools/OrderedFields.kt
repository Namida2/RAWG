package com.example.featureGames.domain.tools

private const val reverseOrderSign = "-"
enum class OrderedFields(val orderingField: String) {
    NAME("name"),
    RELEASED("released"),
    ADDED("added"),
    CREATED("created"),
    UPDATED("updated"),
    RATING("rating"),
    METACRITIC("metacritic")
}

val OrderedFields.reverseOrder: String
get() = reverseOrderSign + this.orderingField

