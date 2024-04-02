package com.github.snuffix.domain.repository

data class Station(
    val id: String,
    val stationId: String,
    val name: String,
    val city: String,
    val country: String,
    val region: String,
    val latitude: Double?,
    val longitude: Double?,
    val hits: Long
) {
    val fullLocation: String = listOf(city, region, country).filter { it.isNotBlank() }.joinToString(", ")
    val hasValidLocationData = latitude != null && longitude != null
}

