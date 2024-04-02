package com.github.snuffix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class StationEntity(
    @PrimaryKey val id: String,
    val stationId: String,
    val name: String,
    val latitude: Double?,
    val longitude: Double?,
    val hits: Long,
    val city: String,
    val country: String,
    val region: String,
)
