package com.example.spacenews.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.spacenews.R
import com.example.spacenews.ui.components.EmptyState
import com.example.spacenews.ui.components.ErrorState
import com.example.spacenews.ui.components.LoadingState
import com.example.spacenews.ui.components.TopBar
import com.example.spacenews.viewmodel.SpaceNewsViewModel
import com.example.spacenews.viewmodel.NewsUiState
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

private fun formatPublishedDate(dateString: String?): String? {
    if (dateString == null) return null
    return try {
        val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        val zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter)
        outputFormatter.format(zonedDateTime)
    } catch (e: Exception) {
        dateString // Fallback to original string if parsing fails
    }
}

@Composable
fun DetailScreen(
    articleId: String?,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: SpaceNewsViewModel
) {
    LaunchedEffect(articleId) {
        articleId?.let { viewModel.fetchArticleDetail(it) }
    }

    val articleState = viewModel.articleDetailUiState
    val article = (articleState as? NewsUiState.Success)?.articles?.firstOrNull()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Column(modifier = modifier.padding(16.dp)) {
        TopBar(
            title = "Details",
            onBackClick = { navController.popBackStack() }
        )

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when (articleState) {
                is NewsUiState.Loading -> {
                    LoadingState(modifier = Modifier.align(Alignment.Center))
                }

                is NewsUiState.Success -> {
                    article?.let {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = it.title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            it.imageUrl?.let { imageUrl ->
                                Spacer(modifier = Modifier.height(12.dp))
                                AsyncImage(
                                    model = imageUrl,
                                    contentDescription = "Article image for ${it.title}",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Column(
                                modifier = Modifier.padding(top = 12.dp)
                            ) {
                                it.newsSite?.let {
                                    Text(
                                        text = "Source: $it",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                formatPublishedDate(it.publishedAt)?.let { formattedDate ->
                                    Text(
                                        text = "Published: $formattedDate",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            it.summary?.let { summary ->
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Summary",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = summary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    lineHeight = 22.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Read full article",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable { uriHandler.openUri(it.url) }
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                is NewsUiState.Error -> {
                    ErrorState(
                        errorMessage = stringResource(R.string.error_article_details),
                        onRetry = { articleId?.let { viewModel.fetchArticleDetail(it) } },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is NewsUiState.Empty -> {
                    EmptyState(message = stringResource(R.string.empty_details))
                }
            }
        }
    }
}