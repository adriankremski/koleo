package com.github.snuffix.koleo.main

enum class StationOptionType {
    DEPARTURE,
    ARRIVAL;

    companion object {
        fun switch(type: StationOptionType) = if (type == DEPARTURE) ARRIVAL else DEPARTURE
    }
}