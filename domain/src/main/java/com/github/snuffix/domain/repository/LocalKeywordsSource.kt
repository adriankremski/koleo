package com.github.snuffix.domain.repository

interface LocalKeywordsSource {
    suspend fun getItems(): List<StationKeyword>
    suspend fun replaceItems(items: List<StationKeyword>): List<StationKeyword>
}