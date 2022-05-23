package com.example.core.domain.entities.filters

import com.example.core.domain.entities.filters.interfaces.Filter
import com.example.core.presentaton.recyclerView.base.BaseRecyclerViewType

data class BaseFilter(
    override val id: String,
    override val name: String,
    override val slug: String?,
    val categoryName: String = "",
    var isSelected: Boolean = false
) : BaseRecyclerViewType, Filter