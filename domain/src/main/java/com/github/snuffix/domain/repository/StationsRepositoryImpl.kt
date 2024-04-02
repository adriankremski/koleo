package com.github.snuffix.domain.repository

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.map
import com.github.michaelbull.result.mapError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class StationsRepositoryImpl @Inject constructor(
    @Named("cacheinfo_stations") val stationsCacheInfo: CacheInfo,
    @Named("cacheinfo_keywords") val keywordsCacheInfo: CacheInfo,
    private val remoteStationsSource: RemoteStationsSource,
    private val localStationsSource: LocalStationsSource,
    private val remoteKeywordsSource: RemoteKeywordsSource,
    private val localKeywordsSource: LocalKeywordsSource,
    private val distanceMatrixSource: DistanceMatrixSource
) : StationsRepository {

    private var cachedStations = mapOf<String, Station>()
    private var cachedKeywords = listOf<StationKeyword>()

    override fun searchForStation(query: String): Flow<Result<List<Station>, RepositoryError>> = flow {
        cachedStations = (cachedStations.takeIf { it.isNotEmpty() } ?: localStationsSource.getItems().associateBy { it.stationId })
        cachedKeywords = cachedKeywords.takeIf { it.isNotEmpty() } ?: localKeywordsSource.getItems()

        println("AdrianTest")
        runSuspendCatching {
            if (cachedStations.isEmpty() || stationsCacheInfo.isExpired()) {
                cachedStations = localStationsSource.replaceItems(remoteStationsSource.getItems()).associateBy { it.stationId }
                stationsCacheInfo.setCacheValid()
            }

            if (cachedKeywords.isEmpty() || keywordsCacheInfo.isExpired()) {
                cachedKeywords = localKeywordsSource.replaceItems(remoteKeywordsSource.getItems())
                keywordsCacheInfo.setCacheValid()
            }

            searchForStation(cachedStations, cachedKeywords, query)
        }.mapError {
            it.printStackTrace()
            RepositoryError.GetStationsItemsError
        }.apply {
            emit(this)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun calculateDistanceInKilometers(start: Station, end: Station): Result<Int, Throwable> {
        return runSuspendCatching { distanceMatrixSource.getDistanceInMeters(start, end) }
            .map { it?.div(1000) ?: 0 }
    }

    private fun searchForStation(
        stations: Map<String, Station>,
        keywords: List<StationKeyword>,
        query: String
    ): List<Station> {
        return keywords.filter {
            it.keyword.startsWith(query, ignoreCase = true)
        }.mapNotNull { keyword -> stations[keyword.stationId] }.distinct()
    }
}