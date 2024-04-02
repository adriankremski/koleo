package com.github.snuffix.koleo.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.snuffix.domain.repository.Station
import com.github.snuffix.koleo.KoleoTheme
import com.github.snuffix.koleo.R
import com.github.snuffix.koleo.main.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState(emptyList())
    val isLoading by viewModel.isLoadingSearchResults.collectAsState()
    val displaySearchError by viewModel.displaySearchError.collectAsState()

    SearchScreenContent(
        modifier = modifier,
        searchQuery = searchQuery,
        searchResults = searchResults,
        search = viewModel::search,
        onStationSelected = viewModel::onStationSelected,
        onDismiss = viewModel::onDismissSearch,
        isLoading = isLoading,
        displaySearchError = displaySearchError
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    displaySearchError: Boolean,
    isLoading: Boolean,
    searchQuery: String,
    searchResults: List<Station>,
    search: (String) -> Unit,
    onStationSelected: (Station) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()

        LaunchedEffect(searchResults) {
            if (searchQuery.isEmpty()) {
                scope.launch {
                    state.animateScrollToItem(0)
                }
            }
        }


        SearchScreenHeader(
            searchQuery = searchQuery,
            onDismiss = onDismiss,
            search = search
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.background,
                )
            }
        } else if (displaySearchError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(id = R.string.no_internet_connection),
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        } else {
            StationsList(searchResults = searchResults) { item ->
                onStationSelected(item)
            }
        }
    }
}

@Composable
fun SearchResultItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    KoleoTheme {
        SearchScreenContentPreview(isLoading = false, displaySearchError = false)
    }
}

@Preview
@Composable
fun LoadingSearchScreenPreview() {
    KoleoTheme {
        SearchScreenContentPreview(isLoading = true, displaySearchError = false)
    }
}

@Preview
@Composable
fun ErrorSearchScreenPreview() {
    KoleoTheme {
        SearchScreenContentPreview(isLoading = false, displaySearchError = true)
    }
}

@Composable
fun SearchScreenContentPreview(isLoading: Boolean, displaySearchError: Boolean) {
    SearchScreenContent(
        searchQuery = "Warszawa",
        searchResults = listOf(
            Station(
                id = "1",
                stationId = "1",
                name = "Warszawa",
                city = "Warszawa",
                country = "Poland",
                region = "Mazowieckie",
                latitude = 52.2297,
                longitude = 21.0122,
                hits = 1
            ),
            Station(
                id = "2",
                stationId = "2",
                name = "Kraków",
                city = "Kraków",
                country = "Poland",
                region = "Małopolskie",
                latitude = 50.0647,
                longitude = 19.9450,
                hits = 1
            ),
        ),
        search = {},
        onStationSelected = {},
        onDismiss = {},
        isLoading = isLoading,
        displaySearchError = displaySearchError
    )
}