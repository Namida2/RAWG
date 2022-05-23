package com.example.core.data.imageLoaders

import com.example.core.data.imageLoaders.interfaces.ImageUrlInfo
import com.example.core.domain.entities.tools.constants.Constants.GAME_PREVIEW_IMAGE_HEIGHT
import com.example.core.domain.entities.tools.constants.Constants.GAME_PREVIEW_IMAGE_WIDTH

data class GameScreenshotUrlInfo(
    override val url: String,
    val page: Int,
    val gameId: Int,
    override val width: Int = GAME_PREVIEW_IMAGE_HEIGHT,
    override val height: Int = GAME_PREVIEW_IMAGE_WIDTH,
    override val needToCenterCrop: Boolean = true
) : ImageUrlInfo