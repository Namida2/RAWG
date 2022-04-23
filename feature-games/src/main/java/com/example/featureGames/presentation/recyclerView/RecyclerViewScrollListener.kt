package com.example.featureGames.presentation.recyclerView

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.core.domain.interfaces.OnPositionChangeListener
import com.example.core.domain.tools.Constants.GAMES_SPAN_COUNT

class RecyclerViewScrollListener(
    recyclerView: RecyclerView,
    onPositionChangeListener: OnPositionChangeListener,
    spanCount: Int = GAMES_SPAN_COUNT
) : RecyclerView.OnScrollListener() {
    init {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm: StaggeredGridLayoutManager =
                    recyclerView.layoutManager as StaggeredGridLayoutManager
                val totalItemCount: Int = lm.itemCount
                val lastVisibleItemPosition = IntArray(spanCount)
                lm.findLastVisibleItemPositions(lastVisibleItemPosition)
                onPositionChangeListener.onNewPosition(lastVisibleItemPosition[0], totalItemCount)
            }
        })
    }

}