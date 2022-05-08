@file:Suppress("ClassName")

package com.example.featureGames.presentation.recyclerView.itemDecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter
import com.example.featureGames.domain.model.GameErrorPagePlaceHolder

class GamesItemDecorations(
    private val topMargin: Int,
    private val largeMargin: Int,
    private val smallMargin: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val adapter = parent.adapter as BaseRecyclerViewAdapter
        val position = parent.getChildAdapterPosition(view)
        if(position == -1) return
        val isGameErrorPagePlaceHolder = adapter.currentList[position] is GameErrorPagePlaceHolder
        outRect.left = smallMargin
        outRect.right = smallMargin
        outRect.bottom = smallMargin
        when (position) {
            0 -> outRect.top = topMargin
            1 -> {
                if (isGameErrorPagePlaceHolder) outRect.top = smallMargin
                else outRect.top = topMargin
            }
            else -> outRect.top = smallMargin

        }
        if ((position == adapter.itemCount - 1 ||
            (adapter.itemCount % 2 == 0 && position == adapter.itemCount - 2)) && !isGameErrorPagePlaceHolder
        ) outRect.bottom = largeMargin
    }

}