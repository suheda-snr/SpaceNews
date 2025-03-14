package com.example.spacenews.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun LoadingState(modifier: Modifier = Modifier, color: Color = colorScheme.primary) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color
    )
}
