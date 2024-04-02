package com.github.snuffix.domain.repository

interface DistanceMatrixSource {
    suspend fun getDistanceInMeters(start: Station, end: Station): Int?
}