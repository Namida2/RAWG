package com.example.core.domain.entities.tools.extensions

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.setIconColorFilter(context: Context, color: Int) {
    this.setColorFilter(
        ContextCompat.getColor(context, color), android.graphics.PorterDuff.Mode.SRC_IN
    )
}