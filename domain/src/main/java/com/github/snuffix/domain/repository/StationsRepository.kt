package com.github.snuffix.domain.repository

import com.github.michaelbull.result.Result
import kotlinx.coroutines.flow.Flow

interface StationsRepository {
    fun searchForStation(query: String): Flow<Result<List<Station>, RepositoryError>>

    suspend fun calculateDistanceInKilometers(start: Station, end: Station): Result<Int, Throwable>
}

sealed class RepositoryError {
    data object GetStationsItemsError: RepositoryError()
}