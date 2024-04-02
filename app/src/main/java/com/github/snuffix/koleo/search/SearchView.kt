package com.github.snuffix.koleo.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.snuffix.koleo.KoleoTheme
import com.github.snuffix.koleo.R

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    value: String,
    onClearClicked: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
) {
    var inputText by remember(value) { mutableStateOf(value) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 10.dp, top = 10.dp, bottom = 10.dp, end = 100.dp)
                .align(Alignment.Center),
            value = inputText,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            onValueChange = { value ->
                inputText = value
                onValueChange(inputText)
            },
        )

        Text(
            modifier = modifier
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .align(Alignment.CenterEnd)
                .clickable { onClearClicked() },
            text = stringResource(id = R.string.search_clear)
        )
    }
}

@Composable
@Preview
fun SearchViewPreview() {
    KoleoTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            SearchView(value = "Search") {}
        }
    }
}