package com.example.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.core.data.database.daos.*
import com.example.core.data.entities.CashInfo
import com.example.core.data.entities.FilterEntity
import com.example.core.data.games.entities.*
import com.example.core.data.games.gameDetailsResponce.*
import com.example.core.data.games.rawGameResponse.AddedByStatus
import com.example.core.domain.games.GameDetailsEntity
import com.example.core.domain.games.GameEntity
import com.example.core.domain.games.MyRating

@Database(
    entities = [
        Tag::class,
        Genre::class,
        Store::class,
        MyRating::class,
        CashInfo::class,
        Platform::class,
        Publisher::class,
        Developer::class,
        GameEntity::class,
        FilterEntity::class,
        TagsForGames::class,
        AddedByStatus::class,
        GenresForGames::class,
        StoresFoeGames::class,
        PlatformsForGames::class,
        ScreenshotsForGame::class,
        GameDetailsEntity::class,
        DevelopersForGames::class,
    ], version = 1, exportSchema = false
)
abstract class Database : RoomDatabase() {
    abstract fun getTagsDao(): TagsDao
    abstract fun getGenresDao(): GenresDao
    abstract fun getStoresDao(): StoresDao
    abstract fun getFiltersDao(): FiltersDao
    abstract fun getCashInfoDao(): CashInfoDao
    abstract fun getMyRatingDao(): MyRatingDao
    abstract fun getPlatformsDao(): PlatformsDao
    abstract fun getPublishersDao(): PublishersDao
    abstract fun getDevelopersDao(): DevelopersDao
    abstract fun getGameEntitiesDao(): GameEntitiesDao
    abstract fun getTagsForGamesDao(): TagsForGamesDao
    abstract fun getAddedByStatusDao(): AddedByStatusDao
    abstract fun getGenresForGamesDao(): GenresForGamesDao
    abstract fun getStoresForGamesDao(): StoresForGamesDao
    abstract fun getGameDetailsEntityDao(): GameDetailsEntityDao
    abstract fun getPlatformsForGamesDao(): PlatformsForGamesDao
    abstract fun getDevelopersForGamesDao(): DevelopersForGamesDao
    abstract fun getScreenshotsForGamesDao(): ScreenshotsForGamesDao
}