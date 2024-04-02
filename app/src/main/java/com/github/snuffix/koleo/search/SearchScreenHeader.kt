package com.github.snuffix.koleo.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.snuffix.koleo.KoleoTheme

@Composable
fun SearchScreenHeader(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onDismiss: () -> Unit,
    search: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clickable {
                    onDismiss()
                }
                .padding(horizontal = 16.dp),
            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
            contentDescription = "back",
            tint = Color.White
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            SearchView(
                value = searchQuery,
                modifier = Modifier.background(
                    Color.White,
                    MaterialTheme.shapes.small
                ),
                onClearClicked = {
                    search("")
                },
                onValueChange = { value ->
                    search(value)
                },
            )
        }
    }
}

@Composable
@Preview
fun SearchScreenHeaderPreview() {
    KoleoTheme {
        SearchScreenHeader(
            searchQuery = "Warsaw",
            onDismiss = {},
            search = {}
        )
    }
}