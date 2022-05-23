package com.example.rawg.domain.useCases

import android.graphics.Bitmap
import com.example.core.data.database.daos.*
import com.example.core.data.games.entities.ScreenshotsForGame
import com.example.core.domain.entities.tools.constants.Constants.PAGE_SIZE
import com.example.core.domain.entities.tools.enums.GameScreenTags
import com.example.core.domain.games.GamesHolder
import com.example.core.domain.games.interfaces.GameScreenItemType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

@AssistedFactory
interface ReadGamesFromLocalStorageUseCaseFactory {
    fun create(tag: GameScreenTags): ReadGamesFromLocalStorageUseCaseImpl
}

class ReadGamesFromLocalStorageUseCaseImpl @AssistedInject constructor(
    private val tagsDao: TagsDao,
    private val storesDao: StoresDao,
    private val genresDao: GenresDao,
    @Assisted val tag: GameScreenTags,
    private val gamesHolder: GamesHolder,
    private val platformsDao: PlatformsDao,
    private val publishersDao: PublishersDao,
    private val developersDao: DevelopersDao,
    private val gameEntitiesDao: GameEntitiesDao,
) : ReadGamesFromLocalStorageUseCase {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var currentJob: Job? = null
    override suspend fun readGames() {
        if (currentJob != null) coroutineScope.coroutineContext.job.cancelChildren()
        currentJob = coroutineScope.launch {
            gameEntitiesDao.readAll()?.onEach { game ->
                game.platforms = platformsDao.readByGameId(game.gameEntity.id)
                game.gameDetails?.stores = storesDao.readByGameId(game.gameEntity.id)
                game.gameDetails?.developers = developersDao.readByGameId(game.gameEntity.id)
                game.gameDetails?.genres = genresDao.readByGameId(game.gameEntity.id)
                game.gameDetails?.tags = tagsDao.readByGameId(game.gameEntity.id)
                game.gameDetails?.publishers = publishersDao.readByGameId(game.gameEntity.id)
                game.shortScreenshots = getMapScreenshots(game.shortScreenshotsUrls)
            }?.chunked(PAGE_SIZE)?.forEachIndexed { page, pageOfGames ->
                gamesHolder.addGames(
                    tag,
                    pageOfGames,
                    GameScreenItemType.GameType(page, pageOfGames.map { it.gameEntity.id }.toMutableList())
                )
            }
        }
        currentJob?.join()
    }

    private fun getMapScreenshots(screenshots: List<ScreenshotsForGame>?): MutableMap<String, Bitmap?>? {
        val result = mutableMapOf<String, Bitmap?>()
        screenshots?.forEach { screenshot ->
            val ulr = screenshot.screenshotUrl
            result[ulr] = null
        }
        return result
    }
}

interface ReadGamesFromLocalStorageUseCase {
    suspend fun readGames()
}