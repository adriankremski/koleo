package com.github.snuffix.data.local.source

import com.github.snuffix.data.local.model.StationKeywordEntity
import com.github.snuffix.domain.repository.LocalKeywordsSource
import com.github.snuffix.domain.repository.StationKeyword

class LocalKeywordsSourceImpl(
    private val keywordsDao: KeywordsDao
) : LocalKeywordsSource {
    override suspend fun getItems(): List<StationKeyword> =
        keywordsDao.getAll().map { it.toDomain() }

    override suspend fun replaceItems(items: List<StationKeyword>): List<StationKeyword> {
        keywordsDao.insert(items.map { it.toEntity() })
        return items
    }
}

fun StationKeywordEntity.toDomain() = StationKeyword(
    id = id,
    keyword = keyword,
    stationId = stationId
)

fun StationKeyword.toEntity() = StationKeywordEntity(
    id = id,
    keyword = keyword,
    stationId = stationId
)
