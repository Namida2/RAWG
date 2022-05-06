package com.example.core.domain.entities.filters

import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class Filter(
    val categoryName: String,
    val name: String,
    val slug: String,
    var isSelected: Boolean = false
) : BaseRecyclerViewType