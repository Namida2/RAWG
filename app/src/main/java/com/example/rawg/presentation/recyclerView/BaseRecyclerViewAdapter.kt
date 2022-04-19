package com.example.rawg.presentation.recyclerView

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import com.example.rawg.domain.tools.Messages.VIEW_TYPE_NOT_FOUND
import java.lang.IllegalArgumentException

class BaseRecyclerViewAdapter(
    private val delegates: List<BaseRecyclerViewDelegate<out BaseRecyclerViewType, out ViewBinding>>
): ListAdapter<BaseRecyclerViewType, BaseViewHolder<BaseRecyclerViewType, ViewBinding>>(
    BaseDiffItemCallback(delegates)
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseRecyclerViewType, ViewBinding> {
        return delegates.find {
            it.layoutId == viewType
        }?.getViewHolder() as BaseViewHolder<BaseRecyclerViewType, ViewBinding>
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<BaseRecyclerViewType, ViewBinding>,
        position: Int
    ) {
        holder.onBind(currentList[position])
    }

    override fun getItemViewType(position: Int): Int {
        val item = currentList[position]
        return delegates.find {
            it.isItMe(item)
        }?.layoutId ?: throw IllegalArgumentException(VIEW_TYPE_NOT_FOUND + item)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}