package com.github.snuffix.data.network.source

import com.squareup.moshi.Json
import retrofit2.http.GET
import retrofit2.http.Query

interface DistanceMatrixApiService {
    @GET("/maps/api/distancematrix/json")
    suspend fun getDistanceMatrix(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") apiKey: String
    ): DistanceMatrixResponse
}

data class DistanceMatrixResponse(
    @Json(name = "destination_addresses") val destinationAddresses: List<String>,
    @Json(name = "origin_addresses") val originAddresses: List<String>,
    val rows: List<Row>?,
    val status: String
)

data class Row(
    val elements: List<Element>?
)

data class Element(
    val distance: Distance?,
    val duration: Duration,
    val status: String
)

data class Distance(
    val text: String,
    val value: Int
)

data class Duration(
    val text: String,
    val value: Int
)