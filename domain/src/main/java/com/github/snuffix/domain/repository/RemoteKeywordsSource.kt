package com.github.snuffix.domain.repository

interface RemoteKeywordsSource {
    suspend fun getItems(): List<StationKeyword>
}