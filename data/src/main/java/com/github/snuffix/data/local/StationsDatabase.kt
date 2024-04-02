package com.github.snuffix.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.snuffix.data.local.model.StationEntity
import com.github.snuffix.data.local.model.StationKeywordEntity
import com.github.snuffix.data.local.source.KeywordsDao
import com.github.snuffix.data.local.source.StationsDao

@Database(entities = [StationEntity::class, StationKeywordEntity::class], version = 1, exportSchema = false)
abstract class StationsDatabase : RoomDatabase() {
    abstract fun stationsDao(): StationsDao
    abstract fun keywordsDao(): KeywordsDao
}