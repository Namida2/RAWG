package com.example.featureGames.data.imageLoaders

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.entities.tools.constants.Constants.GAME_IMAGE_HEIGHT
import com.example.core.domain.entities.tools.constants.Constants.GAME_IMAGE_WIDTH
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.data.imageLoaders.interfaces.ImageUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.core.data.imageLoaders.interfaces.LoadedImageInfo
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import javax.inject.Inject

class GameImageLoader<T: ImageUrlInfo> @Inject constructor(
    private val context: Context
) : ImagesLoader<T> {
    private val imageUrlsInfo = Collections.synchronizedList(mutableListOf<T>())
    override lateinit var onResultHandler: ImagesLoaderResultHandler<T>
    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable -> logE("$this: coroutineContext: $coroutineContext, throwable: $throwable") }
    private val defaultContext = Job() + Main.immediate
    override fun loadImage(
        imageUrlInfo: T,
        coroutineScope: CoroutineScope
    ) {
        imageUrlsInfo.add(imageUrlInfo)
        getImage(imageUrlInfo, coroutineScope)
    }

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        coroutineScope.launch(defaultContext) {
            imageUrlsInfo.forEach { imageUrlInfo ->
                getImage(imageUrlInfo, coroutineScope)
            }
        }
    }

    private fun getImage(
        imageUrlInfo: T,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch(IO + coroutineExceptionHandler) {
            val bitmap = Glide.with(context).asBitmap()
                .apply(RequestOptions().override(GAME_IMAGE_WIDTH, GAME_IMAGE_HEIGHT))
                .load(imageUrlInfo.url).submit().get()
//            logE("getImage: ${imageUrlInfo.gameId}")
            onImageLoaded(imageUrlInfo, bitmap)
        }
    }

    private suspend fun onImageLoaded(imageUrlInfo: T, bitmap: Bitmap) {
        //Use new job to avoid cancellation of passed coroutineContext's job
        withContext(defaultContext) {
            imageUrlsInfo.remove(imageUrlInfo)
            onResultHandler.onImageLoaded(LoadedImageInfo(imageUrlInfo, bitmap))
        }
    }
}


