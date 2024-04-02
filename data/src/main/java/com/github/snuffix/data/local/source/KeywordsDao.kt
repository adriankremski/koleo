package com.github.snuffix.data.local.source

import androidx.room.Dao
import com.github.snuffix.data.local.model.StationKeywordEntity

@Dao
interface KeywordsDao {
    @androidx.room.Query(value = "SELECT * FROM station_keywords")
    suspend fun getAll(): List<StationKeywordEntity>

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationKeywordEntity>)

    @androidx.room.Query(value = "DELETE FROM station_keywords")
    suspend fun deleteAll()
}