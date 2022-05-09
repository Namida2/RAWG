package com.example.core.domain.entities.filters

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class Filter(
    val id: String,
    val name: String,
    val categoryName: String,
    var isSelected: Boolean = false
) : BaseRecyclerViewType