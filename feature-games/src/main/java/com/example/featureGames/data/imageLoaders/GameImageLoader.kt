package com.example.featureGames.data.imageLoaders

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.core.domain.tools.constants.Constants.GAME_IMAGE_HEIGHT
import com.example.core.domain.tools.constants.Constants.GAME_IMAGE_WIDTH
import com.example.core.domain.tools.extensions.logD
import com.example.core.domain.tools.extensions.logE
import com.example.featureGames.data.entities.imageLoader.GameBackgroundImageUrlInfo
import com.example.featureGames.data.imageLoaders.interfaces.ImageUrlInfo
import com.example.featureGames.data.imageLoaders.interfaces.ImagesLoader
import com.example.featureGames.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException
import java.util.*
import javax.inject.Inject

data class LoadedImageInfo<T : ImageUrlInfo>(val imageUrlInfo: T, val bitmap: Bitmap)
typealias GameLoadedImageInfo = LoadedImageInfo<GameBackgroundImageUrlInfo>

class GameImageLoader @Inject constructor(
    private val context: Context
) : ImagesLoader<GameBackgroundImageUrlInfo> {
    private val imagesLinks =
        Collections.synchronizedList(mutableListOf<GameBackgroundImageUrlInfo>())
    override lateinit var onResultHandler: ImagesLoaderResultHandler<GameBackgroundImageUrlInfo>
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            logE("$this: coroutineContext: $coroutineContext, throwable: $throwable")
        }
    private val defaultContext = Job() + Main.immediate
    override fun loadImage(
        imageUrlInfo: GameBackgroundImageUrlInfo,
        coroutineScope: CoroutineScope
    ) {
        imagesLinks.add(imageUrlInfo)
        getImage(imageUrlInfo, coroutineScope)
    }

    override fun onNetworkConnected(coroutineScope: CoroutineScope) {
        coroutineScope.launch(defaultContext) {
            imagesLinks.forEach { imageUrlInfo ->
                getImage(imageUrlInfo, coroutineScope)
            }
        }
    }

    private fun getImage(
        imageUrlInfo: GameBackgroundImageUrlInfo,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch(IO + coroutineExceptionHandler) {
            val bitmap = Glide.with(context).asBitmap()
                .apply(RequestOptions().override(GAME_IMAGE_WIDTH, GAME_IMAGE_HEIGHT))
                .load(imageUrlInfo.link).submit().get()
//            logE("getImage: ${imageUrlInfo.gameId}")
            onImageLoaded(imageUrlInfo, bitmap)
        }
    }

    private suspend fun onImageLoaded(imageUrlInfo: GameBackgroundImageUrlInfo, bitmap: Bitmap) {
        //Use new job to avoid cancellation of passed coroutineContext's job
        withContext(defaultContext) {
            imagesLinks.remove(imageUrlInfo)
            onResultHandler.onImageLoaded(GameLoadedImageInfo(imageUrlInfo, bitmap))
        }
    }
}


