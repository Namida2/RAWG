package com.example.core.presentaton.recyclerView

import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<RecyclerViewItem: BaseRecyclerViewType, ViewBinding: androidx.viewbinding.ViewBinding>(
    private val binding: ViewBinding
): RecyclerView.ViewHolder(binding.root){
    abstract fun onBind(item: RecyclerViewItem)
}