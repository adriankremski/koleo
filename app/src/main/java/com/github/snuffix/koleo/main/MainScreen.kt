package com.github.snuffix.koleo.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.snuffix.koleo.KoleoTheme
import com.github.snuffix.koleo.R
import com.github.snuffix.koleo.search.SearchScreen

@Composable
fun MainScreen(mainViewModel: MainViewModel = hiltViewModel()) {
    val selectedStations by mainViewModel.selectedStations.collectAsState(emptyList())
    val searchState by mainViewModel.searchType.collectAsState(null)
    val displayCalculationProcessWidget by mainViewModel.displayCalculationProcessWidget.collectAsState(false)
    val calculationSteps by mainViewModel.calculationSteps.collectAsState(emptyList())
    val currentCalculationStep by mainViewModel.currentCalculationStep.collectAsState(0)
    val calculationTitleVisibility by mainViewModel.calculationTitleVisibility.collectAsState(false)
    val calculationSubtitleVisibility by mainViewModel.calculationSubtitleVisibility.collectAsState(false)
    val selectStationDialogMessage by mainViewModel.errorMessage.collectAsState(null)
    val distanceInKilometers by mainViewModel.distanceInKilometers.collectAsState(null)

    BackHandler {
        mainViewModel.onDismissSearch()
    }

    MainScreenContent(
        selectedStations = selectedStations,
        showSearch = { option ->
            if (searchState == null) {
                mainViewModel.showSearch(option)
            }
        },
        switchStations = mainViewModel::switchStations,
        onCalculateDistance = mainViewModel::onCalculateDistance,
        displayCalculationProcessWidget = displayCalculationProcessWidget,
        calculationSteps = calculationSteps,
        currentCalculationStep = currentCalculationStep,
        calculationTitleVisibility = calculationTitleVisibility,
        calculationSubtitleVisibility = calculationSubtitleVisibility,
        distanceInKilometers = distanceInKilometers
    )

    AnimatedVisibility(
        visible = searchState != null,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        SearchScreen()
    }

    selectStationDialogMessage?.let { message ->
        KoleoAlertDialog(onDismissRequest = {
            mainViewModel.dismissErrorDialog()
        }, dialogTitle = stringResource(id = R.string.action_required), dialogText = message)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenContent(
    selectedStations: List<StationOption>,
    showSearch: (StationOption) -> Unit,
    switchStations: () -> Unit,
    currentCalculationStep: Int,
    calculationSteps: List<CalculationStep>,
    onCalculateDistance: () -> Unit,
    displayCalculationProcessWidget: Boolean,
    calculationTitleVisibility: Boolean,
    calculationSubtitleVisibility: Boolean,
    distanceInKilometers: String?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                selectedStations.size,
                key = { selectedStations[it].id },
            ) {
                val item = selectedStations[it]

                SelectedStationView(
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable {
                            showSearch(item)
                        },
                    hint = if (item.type == StationOptionType.DEPARTURE) {
                        stringResource(id = R.string.departure)
                    } else {
                        stringResource(id = R.string.arrival)
                    },
                    stationName = item.name,
                    icon = if (item.type == StationOptionType.ARRIVAL) {
                        { modifier ->
                            SwitchStationsIcon(modifier) {
                                switchStations()
                            }
                        }
                    } else {
                        {}
                    }
                )
            }
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = {
                onCalculateDistance()
            },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            )
        ) {
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(32.dp),
                painter = rememberVectorPainter(image = Icons.Default.Search), contentDescription = "calculate"
            )
        }

        if (displayCalculationProcessWidget) {
            ProcessWidget(
                modifier = Modifier.fillMaxSize(),
                currentStep = currentCalculationStep,
                steps = calculationSteps,
                titleVisibility = calculationTitleVisibility,
                subtitleVisibility = calculationSubtitleVisibility,
                distanceInKilometers = distanceInKilometers,
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    KoleoTheme {
        MainScreenContent(
            selectedStations = listOf(),
            showSearch = {},
            switchStations = {},
            currentCalculationStep = 0,
            calculationSteps = emptyList(),
            onCalculateDistance = {},
            displayCalculationProcessWidget = false,
            calculationTitleVisibility = false,
            calculationSubtitleVisibility = false,
            distanceInKilometers = null
        )
    }
}
