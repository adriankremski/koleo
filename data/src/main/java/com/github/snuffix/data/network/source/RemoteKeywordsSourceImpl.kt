package com.github.snuffix.data.network.source

import com.github.snuffix.data.network.model.StationKeywordDTO
import com.github.snuffix.domain.repository.RemoteKeywordsSource
import com.github.snuffix.domain.repository.StationKeyword

class RemoteKeywordsSourceImpl(
    private val apiService: KoleoApiService,
) : RemoteKeywordsSource {
    override suspend fun getItems(): List<StationKeyword> {
        return apiService.getKeywords().map(StationKeywordDTO::toDomainModel)
    }
}

fun StationKeywordDTO.toDomainModel() = StationKeyword(
    id = id,
    keyword = keyword,
    stationId = stationId
)
