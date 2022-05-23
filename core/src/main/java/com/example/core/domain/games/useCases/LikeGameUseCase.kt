package com.example.core.domain.games.useCases

import com.example.core.data.database.daos.*
import com.example.core.data.games.entities.*
import com.example.core.domain.games.Game
import com.example.core.domain.games.GamesHolder
import com.example.core.domain.entities.tools.extensions.logD
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import javax.inject.Inject

class LikeGameUseCaseImpl @Inject constructor(
    private val tagsDao: TagsDao,
    private val storesDao: StoresDao,
    private val genresDao: GenresDao,
    private val gamesHolder: GamesHolder,
    private val myRatingDao: MyRatingDao,
    private val platformsDao: PlatformsDao,
    private val publishersDao: PublishersDao,
    private val developersDao: DevelopersDao,
    private val tagsForGames: TagsForGamesDao,
    private val gameEntitiesDao: GameEntitiesDao,
    private val storesForGamesDao: StoresForGamesDao,
    private val genresForGamesDao: GenresForGamesDao,
    private val gameDetailsEntityDao: GameDetailsEntityDao,
    private val platformsForGamesDao: PlatformsForGamesDao,
    private val developersForGamesDao: DevelopersForGamesDao,
    private val publishersForGamesDao: PublishersForGamesDao,
    private val screenshotsForGamesDao: ScreenshotsForGamesDao,
    private val addedByStatusDao: AddedByStatusDao
) : LikeGameUseCase {
    private val coroutineScope = CoroutineScope(SupervisorJob() + IO)
    private var currentJob: Job? = null
    override fun likeGame(game: Game) {
        if (currentJob != null) coroutineScope.coroutineContext.job.cancelChildren()
        currentJob = coroutineScope.launch {
            gameEntitiesDao.insert(game.gameEntity.copy(isLiked = true))
            game.addedByStatus?.let { addedByStatusDao.insert(it) }
            game.shortScreenshotsUrls?.let { screenshotsForGamesDao.insert(*it.toTypedArray()) }
            insertDetails(coroutineScope, game)
            gamesHolder.changeGameLikeStatus(game, true)
            logD("COMPLETED")
        }
    }

    override suspend fun insertDetails(coroutineScope: CoroutineScope, game: Game) {
        coroutineScope.launch {
            game.gameDetails?.gameDetailsEntity?.let { gameDetailsEntityDao.insert(it) }
            game.gameDetails?.ratings?.let { myRatingDao.insert(*it.toTypedArray()) }
            game.platforms?.let { platformsDao.insert(*it.toTypedArray()) }
            game.gameDetails?.stores?.let { storesDao.insert(*it.toTypedArray()) }
            game.gameDetails?.developers?.let { developersDao.insert(*it.toTypedArray()) }
            game.gameDetails?.developers?.let { developersDao.insert(*it.toTypedArray()) }
            game.gameDetails?.genres?.let { genresDao.insert(*it.toTypedArray()) }
            game.gameDetails?.tags?.let { tagsDao.insert(*it.toTypedArray()) }
            game.gameDetails?.publishers?.let { publishersDao.insert(*it.toTypedArray()) }
            game.gameDetails?.developers?.map {
                DevelopersForGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                developersForGamesDao.insert(*it.toTypedArray())
            }
            game.gameDetails?.genres?.map {
                GenresForGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                genresForGamesDao.insert(*it.toTypedArray())
            }
            game.platforms?.map {
                PlatformsForGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                platformsForGamesDao.insert(*it.toTypedArray())
            }
            game.gameDetails?.stores?.map {
                StoresFoeGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                storesForGamesDao.insert(*it.toTypedArray())
            }
            game.gameDetails?.tags?.map {
                TagsForGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                tagsForGames.insert(*it.toTypedArray())
            }
            game.gameDetails?.publishers?.map {
                PublishersForGames(game.gameEntity.id, it.id)
            }.also {
                if (it == null) return@also
                publishersForGamesDao.insert(*it.toTypedArray())
            }
        }
    }

    override fun unlikeGame(game: Game) {
        if (currentJob != null) coroutineScope.coroutineContext.job.cancelChildren()
        currentJob = coroutineScope.launch {
            val gameId = game.gameEntity.id
            gameEntitiesDao.delete(gameId)
            addedByStatusDao.delete(gameId)
            screenshotsForGamesDao.delete(gameId)
            gameDetailsEntityDao.delete(gameId)
            myRatingDao.delete(gameId)
            developersForGamesDao.delete(gameId)
            genresForGamesDao.delete(gameId)
            platformsForGamesDao.delete(gameId)
            storesForGamesDao.delete(gameId)
            tagsForGames.delete(gameId)
            publishersForGamesDao.delete(gameId)
            gamesHolder.changeGameLikeStatus(game, false)
            logD("DELETED")
        }
    }
}

interface LikeGameUseCase {
    fun likeGame(game: Game)
    fun unlikeGame(game: Game)
    suspend fun insertDetails(coroutineScope: CoroutineScope, game: Game)
}
