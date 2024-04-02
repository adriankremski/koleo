package com.github.snuffix.koleo.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.snuffix.domain.repository.Station
import com.github.snuffix.koleo.KoleoTheme

@Composable
fun StationsList(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    searchResults: List<Station>,
    onStationSelected: (Station) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        state = state,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            searchResults.size,
            key = { searchResults[it].id },
        ) {
            val item = searchResults[it]

            SearchResultItem(
                modifier = Modifier
                    .clickable {
                        onStationSelected(item)
                    },
                title = item.name,
                subtitle = item.fullLocation
            )
        }
    }
}

@Composable
@Preview
fun StationsListPreview() {
    KoleoTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            StationsList(
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
                onStationSelected = {}
            )
        }
    }
}
