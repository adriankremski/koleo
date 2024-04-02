package com.github.snuffix.koleo.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectedStationView(
    modifier: Modifier = Modifier,
    hint: String,
    stationName: String?,
    icon: @Composable (Modifier) -> Unit
) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Text(
            text = stationName ?: hint,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp),
            color = if (stationName != null) Color.Black else Color.Gray
        )

        icon(
            Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        )
    }
}
