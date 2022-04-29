package com.example.featureGames.data.imageLoaders.interfaces

import androidx.annotation.MainThread
import com.example.featureGames.data.imageLoaders.LoadedImageInfo
import kotlinx.coroutines.CoroutineScope

interface ImagesLoaderResultHandler<T : ImageUrlInfo> {
    suspend fun onImageLoaded(loadedImageInfo: LoadedImageInfo<T>)
}

interface ImagesLoader<T : ImageUrlInfo> {
    fun loadImage(imageUrlInfo: T, coroutineScope: CoroutineScope)
    var onResultHandler: ImagesLoaderResultHandler<T>
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}