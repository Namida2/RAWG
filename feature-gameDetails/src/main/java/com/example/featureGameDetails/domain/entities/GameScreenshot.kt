package com.example.featureGameDetails.domain.entities

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.Drawable
import com.example.core.presentaton.recyclerView.BaseRecyclerViewType

data class GameScreenshot(val screenshot: Drawable): BaseRecyclerViewType