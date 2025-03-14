package com.example.spacenews.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spacenews.R

@Composable
fun ArticleContent(
    summary: String?,
    url: String,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    Column(modifier = modifier) {
        summary?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Texts(
                text = it,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Texts(
            text = stringResource(R.string.read_full_article),
            color = colorScheme.secondary,
            style = typography.labelMedium,
            modifier = Modifier
                .clickable { uriHandler.openUri(url) }
                .padding(vertical = 4.dp)
                .align(Alignment.End)
        )
    }
}