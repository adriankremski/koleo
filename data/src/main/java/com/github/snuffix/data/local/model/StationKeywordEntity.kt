package com.github.snuffix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "station_keywords")
data class StationKeywordEntity(
    @PrimaryKey val id: String,
    val keyword: String,
    val stationId: String,
)
