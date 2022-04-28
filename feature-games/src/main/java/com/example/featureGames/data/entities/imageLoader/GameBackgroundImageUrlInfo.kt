package com.example.featureGames.data.entities.imageLoader

import com.example.featureGames.data.imageLoaders.interfaces.ImageUrlInfo

data class GameBackgroundImageUrlInfo(
    override val link: String,
    val page: Int,
    val gameId: Int
): ImageUrlInfo