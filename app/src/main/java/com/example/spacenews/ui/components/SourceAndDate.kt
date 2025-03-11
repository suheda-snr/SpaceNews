package com.example.spacenews.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spacenews.utils.DateUtils

@Composable
fun SourceAndDate(
    newsSite: String?,
    publishedAt: String?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        newsSite?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Texts(
                text = "Source: $it",
                color = MaterialTheme.colorScheme.secondary,
                style = typography.labelSmall.copy(fontSize = 12.sp)
            )
        }
        DateUtils.formatPublishedDate(publishedAt)?.let { date ->
            Texts(
                text = "Published: $date",
                color = MaterialTheme.colorScheme.secondary,
                style = typography.labelSmall.copy(fontSize = 12.sp)
            )
        }
    }
}