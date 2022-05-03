package com.example.core.presentaton.recyclerView

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding

class BaseDiffItemCallback(
    private val delegates: List<RecyclerViewAdapterDelegate<out BaseRecyclerViewType, out ViewBinding>>
) : DiffUtil.ItemCallback<BaseRecyclerViewType>() {
    override fun areItemsTheSame(
        oldItem: BaseRecyclerViewType,
        newItem: BaseRecyclerViewType
    ): Boolean =
        if (newItem::class != oldItem::class) false
        else getDiffItemCallback(newItem).areItemsTheSame(newItem, oldItem)

    override fun areContentsTheSame(
        oldItem: BaseRecyclerViewType,
        newItem: BaseRecyclerViewType
    ): Boolean =
        if (newItem::class != oldItem::class) false
        else getDiffItemCallback(newItem).areContentsTheSame(newItem, oldItem)

    private fun getDiffItemCallback(item: BaseRecyclerViewType): DiffUtil.ItemCallback<BaseRecyclerViewType> =
        delegates.find {
            it.isItMe(item)
        }?.getDiffItemCallback() as DiffUtil.ItemCallback<BaseRecyclerViewType>

}