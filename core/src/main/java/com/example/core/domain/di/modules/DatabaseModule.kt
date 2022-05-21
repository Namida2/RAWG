package com.example.core.domain.di.modules

import com.example.core.data.database.Database
import com.example.core.data.database.daos.*
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    fun provideFiltersDao(database: Database): FiltersDao =
        database.getFiltersDao()
    @Provides
    fun provideCashInfoDao(database: Database): CashInfoDao =
        database.getCashInfoDao()
    @Provides
    fun provideAddedByStatusDao(database: Database): AddedByStatusDao =
        database.getAddedByStatusDao()
    @Provides
    fun provideTagsDao(database: Database): TagsDao =
        database.getTagsDao()
    @Provides
    fun provideGenresDao(database: Database): GenresDao =
        database.getGenresDao()
    @Provides
    fun provideStoresDao(database: Database): StoresDao =
        database.getStoresDao()
    @Provides
    fun provideMyRatingDao(database: Database): MyRatingDao =
        database.getMyRatingDao()
    @Provides
    fun providePlatformsDao(database: Database): PlatformsDao =
        database.getPlatformsDao()
    @Provides
    fun providePublishersDao(database: Database): PublishersDao =
        database.getPublishersDao()
    @Provides
    fun provideDevelopersDao(database: Database): DevelopersDao =
        database.getDevelopersDao()
    @Provides
    fun provideGameEntitiesDao(database: Database): GameEntitiesDao =
        database.getGameEntitiesDao()
    @Provides
    fun provideGameDetailsEntityDao(database: Database): GameDetailsEntityDao =
        database.getGameDetailsEntityDao()
    @Provides
    fun provideScreenshotsForGamesDao(database: Database): ScreenshotsForGamesDao =
        database.getScreenshotsForGamesDao()
    @Provides
    fun provideDevelopersForGamesDao(database: Database): DevelopersForGamesDao =
        database.getDevelopersForGamesDao()
    @Provides
    fun provideGenresForGamesDao(database: Database): GenresForGamesDao =
        database.getGenresForGamesDao()
    @Provides
    fun providePlatformsForGamesDao(database: Database): PlatformsForGamesDao =
        database.getPlatformsForGamesDao()
    @Provides
    fun provideStoresForGamesDao(database: Database): StoresForGamesDao =
        database.getStoresForGamesDao()
    @Provides
    fun provideTagsForGamesDao(database: Database): TagsForGamesDao =
        database.getTagsForGamesDao()
    @Provides
    fun providePublishersForGamesDao(database: Database): PublishersForGamesDao =
        database.getPublishersForGamesDao()
}