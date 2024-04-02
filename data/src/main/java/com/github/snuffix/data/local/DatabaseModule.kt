package com.github.snuffix.data.local

import android.content.Context
import androidx.room.Room
import com.github.snuffix.data.local.source.KeywordsDao
import com.github.snuffix.data.local.source.LocalKeywordsSourceImpl
import com.github.snuffix.data.local.source.LocalStationsSourceImpl
import com.github.snuffix.data.local.source.StationsDao
import com.github.snuffix.domain.repository.LocalKeywordsSource
import com.github.snuffix.domain.repository.LocalStationsSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): StationsDatabase =
        Room.databaseBuilder(
            context,
            StationsDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    @Singleton
    fun provideStationsDao(database: StationsDatabase): StationsDao = database.stationsDao()

    @Provides
    @Singleton
    fun provideKeywordsDao(database: StationsDatabase): KeywordsDao = database.keywordsDao()

    @Provides
    @Singleton
    fun provideStationsLocalSource(
        dao: StationsDao
    ): LocalStationsSource = LocalStationsSourceImpl(dao)

    @Provides
    @Singleton
    fun provideKeywordsLocalSource(
        dao: KeywordsDao
    ): LocalKeywordsSource = LocalKeywordsSourceImpl(dao)
}