package com.example.core.presentaton.recyclerView.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

interface RecyclerViewAdapterDelegate<RecyclerViewItem: BaseRecyclerViewType, Binding: ViewBinding> {
    val layoutId: Int
    fun isItMe(item: BaseRecyclerViewType): Boolean
    fun getViewHolder(inflater: LayoutInflater, container: ViewGroup): BaseViewHolder<RecyclerViewItem, Binding>
    fun getDiffItemCallback(): DiffUtil.ItemCallback<RecyclerViewItem>
}