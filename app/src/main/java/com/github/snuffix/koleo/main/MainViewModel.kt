package com.github.snuffix.koleo.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.github.snuffix.domain.repository.Station
import com.github.snuffix.domain.repository.StationsRepository
import com.github.snuffix.domain.repository.RepositoryError
import com.github.snuffix.koleo.secondary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StationsRepository
) : ViewModel() {
    val searchQuery = MutableStateFlow("")

    val isLoadingSearchResults = MutableStateFlow(false)

    val displaySearchError = MutableStateFlow(false)

    val errorMessage = MutableStateFlow<String?>(null)

    val selectedStations = MutableStateFlow(
        listOf(
            StationOption(
                type = StationOptionType.DEPARTURE
            ),
            StationOption(
                type = StationOptionType.ARRIVAL
            ),
        )
    )

    val searchResults = MutableStateFlow(emptyList<Station>())
    val searchType = MutableStateFlow<StationOptionType?>(null)

    var currentCalculationStep = MutableStateFlow(0)
    val calculationSteps = MutableStateFlow(emptyList<CalculationStep>())
    val displayCalculationProcessWidget = MutableStateFlow(false)
    val calculationTitleVisibility = MutableStateFlow(false)
    val calculationSubtitleVisibility = MutableStateFlow(false)
    val distanceInKilometers = MutableStateFlow<String?>(null)

    private val processSteps = listOf(
        CalculationStep(
            title = "Step #1",
            stateText = "Doing something"
        ),
        CalculationStep(
            title = "Step #2",
            stateText = "Doing something"
        ),
        CalculationStep(
            title = "Finished",
            stateText = "Click to see the result"
        ),
    )

    private var calculationJob: Job? = null

    init {
        searchQuery
            .buffer(onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .onEach {
                displaySearchError.value = false
                isLoadingSearchResults.value = true
            }
            .flatMapConcat { repository.searchForStation(it) }
            .onEach { result ->
                isLoadingSearchResults.value = false

                result
                    .onSuccess {
                        searchResults.value = it
                    }.onFailure { error ->
                        when (error) {
                            is RepositoryError.GetStationsItemsError -> {
                                displaySearchError.value = true
                            }
                        }
                    }

            }
            .launchIn(viewModelScope)
    }

    fun search(query: String) {
        searchQuery.value = query
    }

    fun showSearch(selectedStation: StationOption) {
        displaySearchError.value = false
        search(selectedStation.name ?: "")
        searchType.value = selectedStation.type
    }

    fun onDismissSearch() {
        searchType.value = null
    }

    fun onStationSelected(selectedStation: Station) {
        resetCalculationWidget()
        searchType.value?.let { type ->
            selectedStations.value = selectedStations.value.map { station ->
                if (station.type == type) {
                    station.copy(station = selectedStation)
                } else {
                    station
                }
            }
        }
        searchType.value = null
    }

    fun switchStations() {
        resetCalculationWidget()
        selectedStations.value = selectedStations.value.map { station ->
            station.copy(type = StationOptionType.switch(station.type))
        }.reversed()
    }

    fun onCalculateDistance() {
        if (displayCalculationProcessWidget.value) {
            return
        }

        // Check that both stations are selected
        selectedStations.value.firstOrNull { it.station == null }?.let { station ->
            when (station.type) {
                StationOptionType.DEPARTURE -> errorMessage.value = "Please select departure station"
                StationOptionType.ARRIVAL -> errorMessage.value = "Please select arrival station"
            }
            return
        }

        // Check that both stations have valid location data
        selectedStations.value.firstOrNull { it.station?.hasValidLocationData == false }?.let {
            errorMessage.value = "Selected station ${it.station?.name} has no location data"
            return
        }

        // Start calculation
        viewModelScope.launch {
            displayCalculationProcessWidget.value = true
            calculationSteps.value = processSteps

            launch {
                calculationTitleVisibility.value = true
                delay(300)
                calculationSubtitleVisibility.value = true
            }

            // Mocking long running operation
            repeat(2) {
                delay(1000)
                currentCalculationStep.value = it
            }

            // Real calculation starts here
            repository.calculateDistanceInKilometers(
                selectedStations.value.first { it.type == StationOptionType.DEPARTURE }.station!!,
                selectedStations.value.first { it.type == StationOptionType.ARRIVAL }.station!!
            ).onSuccess {
                distanceInKilometers.value = "$it km"
            }.onFailure {
                distanceInKilometers.value = "Could not calculate distance"
            }

            currentCalculationStep.value = 2
        }
    }

    private fun resetCalculationWidget() {
        distanceInKilometers.value = null
        calculationJob?.cancel()
        displayCalculationProcessWidget.value = false
        calculationTitleVisibility.value = false
        calculationSubtitleVisibility.value = false
        currentCalculationStep.value = 0
    }

    fun dismissErrorDialog() {
        errorMessage.value = null
    }
}