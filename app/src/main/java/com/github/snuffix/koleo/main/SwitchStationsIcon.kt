package com.github.snuffix.koleo.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SwitchStationsIcon(modifier: Modifier = Modifier, switchStations: () -> Unit) {
    Icon(
        modifier = modifier
            .padding(8.dp)
            .clickable {
                switchStations()
            },
        imageVector = Icons.Outlined.KeyboardArrowUp,
        contentDescription = "switchIcon"
    )
}