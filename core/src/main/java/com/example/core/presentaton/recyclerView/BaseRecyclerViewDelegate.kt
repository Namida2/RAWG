package com.example.core.presentaton.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import java.util.zip.Inflater

interface BaseRecyclerViewDelegate<RecyclerViewItem: BaseRecyclerViewType, Binding: ViewBinding> {
    val layoutId: Int
    fun isItMe(item: BaseRecyclerViewType): Boolean
    fun getViewHolder(inflater: LayoutInflater, parent: ViewGroup): BaseViewHolder<RecyclerViewItem, Binding>
    fun getDiffItemCallback(): DiffUtil.ItemCallback<RecyclerViewItem>
}