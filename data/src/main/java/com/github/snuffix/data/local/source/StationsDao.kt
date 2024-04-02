package com.github.snuffix.data.local.source

import androidx.room.Dao
import com.github.snuffix.data.local.model.StationEntity

@Dao
interface StationsDao {
    @androidx.room.Query(value = "SELECT * FROM stations ORDER BY hits DESC")
    suspend fun getAll(): List<StationEntity>

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(stations: List<StationEntity>)

    @androidx.room.Query(value = "DELETE FROM stations")
    suspend fun deleteAll()
}