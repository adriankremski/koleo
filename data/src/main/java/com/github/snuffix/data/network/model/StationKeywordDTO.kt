package com.github.snuffix.data.network.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

@Keep
class StationKeywordDTO(
    @Json(name = "id") val id: String,
    @Json(name = "keyword") val keyword: String,
    @Json(name = "station_id") val stationId: String,
)
