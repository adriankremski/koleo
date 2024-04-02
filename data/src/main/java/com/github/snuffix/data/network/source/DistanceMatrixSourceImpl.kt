package com.github.snuffix.data.network.source

import android.location.Location
import android.util.Log
import com.github.michaelbull.result.coroutines.runSuspendCatching
import com.github.michaelbull.result.get
import com.github.michaelbull.result.mapError
import com.github.snuffix.domain.repository.Constants.KEY_GOOGLE_MAPS_API_KEY
import com.github.snuffix.domain.repository.DistanceMatrixSource
import com.github.snuffix.domain.repository.Station
import javax.inject.Inject
import javax.inject.Named

class DistanceMatrixSourceImpl @Inject constructor(
    @Named(KEY_GOOGLE_MAPS_API_KEY) private val apiKey: String,
    private val apiService: DistanceMatrixApiService,
) : DistanceMatrixSource {
    override suspend fun getDistanceInMeters(start: Station, end: Station): Int? {
        val startLatitude = start.latitude!!
        val startLongitude = start.longitude!!
        val endLatitude = end.latitude!!
        val endLongitude = end.longitude!!

        return runSuspendCatching {
            apiService.getDistanceMatrix(
                origins = "$startLatitude,$startLongitude",
                destinations = "$endLatitude,$endLongitude",
                apiKey = apiKey,
            ).rows?.firstOrNull()?.elements?.firstOrNull()?.distance?.value!!
        }.mapError {
            val resultArray = FloatArray(1)
            Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, resultArray)
            return resultArray.firstOrNull()?.toInt()
        }.get()
    }
}