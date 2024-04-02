package com.github.snuffix.domain.repository

interface LocalStationsSource {
    suspend fun getItems(): List<Station>
    suspend fun replaceItems(items: List<Station>): List<Station>
}