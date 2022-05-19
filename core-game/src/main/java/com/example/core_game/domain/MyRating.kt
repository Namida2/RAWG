package com.example.core_game.domain

data class MyRating(val title: String, val count: Int) : Comparable<MyRating> {
    override fun compareTo(other: MyRating): Int = other.count - this.count
}