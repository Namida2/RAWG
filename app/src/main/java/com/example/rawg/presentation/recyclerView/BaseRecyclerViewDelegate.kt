package com.example.rawg.presentation.recyclerView

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

interface BaseRecyclerViewDelegate<RecyclerViewItem: BaseRecyclerViewType, Binding: ViewBinding> {
    val layoutId: Int
    fun isItMe(item: BaseRecyclerViewType): Boolean
    fun getViewHolder(): BaseViewHolder<RecyclerViewItem, Binding>
    fun getDiffItemCallback(): DiffUtil.ItemCallback<RecyclerViewItem>
}