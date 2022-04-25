package com.example.core.domain.interfaces

interface OnPositionChangeListener {
    fun onNewPosition(positions: IntArray, itemCount: Int)
}