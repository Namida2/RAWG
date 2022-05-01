package com.example.core.domain.entities

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestFilters @Inject constructor() {
    var ordering: List<Filter> = emptyList()
    var platforms: List<Filter> = emptyList()
    var stores: List<Filter> = emptyList()
    var developers: List<Filter> = emptyList()
    var genres:  List<Filter> = emptyList()
    var publishers: List<Filter> = emptyList()
    var tags: List<Filter> = emptyList()
}