package com.github.snuffix.domain.repository

interface RemoteStationsSource {
    suspend fun getItems(): List<Station>
}