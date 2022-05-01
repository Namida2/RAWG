package com.example.rawg.data.model

class Root<T> {
    var count = 0
    var next: String? = null
    var previous: String? = null
    var results: ArrayList<T>? = null
}
