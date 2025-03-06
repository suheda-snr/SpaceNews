package com.example.spacenews.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.spacenews.model.SpaceNewsArticle


@Composable
fun NewsItem(article: SpaceNewsArticle, modifier: Modifier = Modifier) {
    Texts(
        text = article.title,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}