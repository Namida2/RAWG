@file:Suppress("ClassName")

package com.example.featureGames.presentation.recyclerView.itemDecorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.core.presentaton.recyclerView.BaseRecyclerViewAdapter

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
        when (position) {
            0 -> {
                outRect.top = topMargin
                outRect.left = smallMargin
                outRect.right = smallMargin
            }
            1 -> {
                outRect.top = topMargin
                outRect.left = smallMargin
                outRect.right = smallMargin
            }
            else -> {
                outRect.top = largeMargin
                outRect.left = smallMargin
                outRect.right = smallMargin
            }
        }
        if (position == adapter.itemCount - 1 || (adapter.itemCount % 2 == 0 && position == adapter.itemCount - 2))
            outRect.bottom = largeMargin
    }
}