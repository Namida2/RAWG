package com.example.core.data.imageLoaders.interfaces

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineScope

data class LoadedImageInfo<T : ImageUrlInfo>(val imageUrlInfo: T, val bitmap: Bitmap)
interface ImagesLoaderResultHandler<T : ImageUrlInfo> {
    suspend fun onImageLoaded(loadedImageInfo: LoadedImageInfo<T>)
}

interface ImagesLoader<T : ImageUrlInfo> {
    fun loadImage(imageUrlInfo: T, coroutineScope: CoroutineScope)
    var onResultHandler: ImagesLoaderResultHandler<T>
    fun onNetworkConnected(coroutineScope: CoroutineScope)
}