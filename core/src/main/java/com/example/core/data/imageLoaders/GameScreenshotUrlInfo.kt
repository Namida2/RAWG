package com.example.core.data.imageLoaders

import com.example.core.data.imageLoaders.interfaces.ImageUrlInfo

data class GameScreenshotUrlInfo(
    override val url: String,
    val page: Int,
    val gameId: Int
): ImageUrlInfo