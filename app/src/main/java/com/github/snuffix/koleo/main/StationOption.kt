package com.github.snuffix.koleo.main

import com.github.snuffix.domain.repository.Station
import java.util.UUID

data class StationOption(
    val id: String = UUID.randomUUID().toString(),
    val type: StationOptionType,
    val station: Station? = null
) {
    val name: String?
        get() = station?.name
}