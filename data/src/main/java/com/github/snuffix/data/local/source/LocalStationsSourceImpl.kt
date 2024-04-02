package com.github.snuffix.data.local.source

import com.github.snuffix.data.local.model.StationEntity
import com.github.snuffix.domain.repository.Station
import com.github.snuffix.domain.repository.LocalStationsSource

class LocalStationsSourceImpl(
    private val dao: StationsDao,
) : LocalStationsSource {
    override suspend fun getItems(): List<Station> {
        return dao.getAll().map(StationEntity::toDomain)
    }

    override suspend fun replaceItems(items: List<Station>): List<Station> {
        dao.deleteAll()
        dao.insert(items.map(Station::toEntity))
        return getItems()
    }
}

fun Station.toEntity() = StationEntity(
    id = id,
    stationId = stationId,
    name = name,
    city = city,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    hits = hits,
)

fun StationEntity.toDomain() = Station(
    id = id,
    stationId = stationId,
    name = name,
    city = city,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    hits = hits,
)
