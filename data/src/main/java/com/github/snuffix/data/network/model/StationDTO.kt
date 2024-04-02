package com.github.snuffix.data.network.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class StationDTO(
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "city") val city: String,
    @Json(name = "country") val country: String,
    @Json(name = "region") val region: String,
    @Json(name = "latitude") val latitude: Double?,
    @Json(name = "longitude") val longitude: Double?,
    @Json(name = "hits") val hits: Long,
)
