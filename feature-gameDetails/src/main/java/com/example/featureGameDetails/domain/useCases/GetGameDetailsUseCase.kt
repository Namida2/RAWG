package com.example.featureGameDetails.domain.useCases

import android.graphics.Bitmap
import com.example.core.data.games.gameDetailsResponce.GameDetailsResponse
import com.example.core.data.imageLoaders.GameScreenshotUrlInfo
import com.example.core.data.imageLoaders.interfaces.ImagesLoader
import com.example.core.data.imageLoaders.interfaces.ImagesLoaderResultHandler
import com.example.core.data.imageLoaders.interfaces.LoadedImageInfo
import com.example.core.domain.entities.tools.extensions.logE
import com.example.core.domain.games.Game
import com.example.core.domain.games.GameDetails
import com.example.core.domain.games.GamesHolder
import com.example.featureGameDetails.domain.repositories.GameDetailsService
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@AssistedFactory
interface GetGameDetailsUseCaseFactory {
    fun create(coroutineScope: CoroutineScope): GetGameDetailsUseCaseImpl
}

class GetGameDetailsUseCaseImpl @AssistedInject constructor(
    private val gamesHolder: GamesHolder,
    private val gameDetailsService: GameDetailsService,
    @Assisted private var coroutineScope: CoroutineScope,
    private val imagesLoader: ImagesLoader<GameScreenshotUrlInfo>,
    private val mapper: GameDetailsResponse.Mapper<GameDetails>
) : GetGameDetailsUseCase, ImagesLoaderResultHandler<GameScreenshotUrlInfo> {

    init {
        imagesLoader.onResultHandler = this
    }

    private val _onNewScreenshotLoaded =
        MutableSharedFlow<Bitmap>(onBufferOverflow = BufferOverflow.SUSPEND)
    override val onNewScreenshotLoaded: SharedFlow<Bitmap> = _onNewScreenshotLoaded

    override suspend fun getGameDetails(gameId: Int): Game {
        val game = gamesHolder.getGameById(gameId).also { loadImages(it) }
        if (game.gameDetails != null) return game
        logE("LOADING DETAILS")
        return game.also {
            it.gameDetails = mapper.map(
                gameDetailsService.getDetails(gameId)
            )
        }
    }

    override fun onNetworkConnected(gameId: Int) {
        coroutineScope.coroutineContext[CoroutineExceptionHandler]
        imagesLoader.onNetworkConnected(coroutineScope)
    }

    override suspend fun onImageLoaded(loadedImageInfo: LoadedImageInfo<GameScreenshotUrlInfo>) {
        _onNewScreenshotLoaded.emit(loadedImageInfo.bitmap)
        gamesHolder.addShortGameScreenshot(
            loadedImageInfo.imageUrlInfo.gameId,
            loadedImageInfo.imageUrlInfo.url,
            loadedImageInfo.bitmap
        )
    }

    private fun loadImages(game: Game) {
        game.shortScreenshots?.let {
            if (!it.values.contains(null)) return
        }
        game.shortScreenshots?.forEach { (url, bitmap) ->
            if (bitmap != null) return@forEach
            imagesLoader.loadImage(
                GameScreenshotUrlInfo(url, 0, game.gameEntity.id),
                coroutineScope
            )
        }
    }
}

interface GetGameDetailsUseCase {
    val onNewScreenshotLoaded: SharedFlow<Bitmap>
    suspend fun getGameDetails(gameId: Int): Game
    fun onNetworkConnected(gameId: Int)
}