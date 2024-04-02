package com.github.snuffix.data.network.source

import com.github.snuffix.data.network.model.StationDTO
import com.github.snuffix.domain.repository.Station
import com.github.snuffix.domain.repository.RemoteStationsSource
import java.util.UUID

class RemoteStationsSourceImpl(
    private val apiService: KoleoApiService,
) : RemoteStationsSource {
    override suspend fun getItems(): List<Station> {
        return apiService.getStations().map(StationDTO::toDomainModel)
    }
}

fun StationDTO.toDomainModel() = Station(
    id = UUID.randomUUID().toString(),
    stationId = id,
    name = name,
    city = city,
    country = country,
    region = region,
    latitude = latitude,
    longitude = longitude,
    hits = hits,
)
