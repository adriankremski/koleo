package com.github.snuffix.data.network.source

import com.github.snuffix.data.network.model.StationDTO
import com.github.snuffix.data.network.model.StationKeywordDTO
import retrofit2.http.GET
import retrofit2.http.Headers

interface KoleoApiService {
    @GET("main/stations")
    @Headers("X-KOLEO-Version: 1")
    suspend fun getStations(): List<StationDTO>

    @GET("main/station_keywords")
    @Headers("X-KOLEO-Version: 1")
    suspend fun getKeywords(): List<StationKeywordDTO>
}