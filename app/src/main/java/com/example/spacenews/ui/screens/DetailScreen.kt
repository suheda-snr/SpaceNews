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
import com.example.spacenews.ui.components.Texts
import com.example.spacenews.ui.components.TopBar
import com.example.spacenews.utils.DateUtils
import com.example.spacenews.viewmodel.SpaceNewsViewModel
import com.example.spacenews.viewmodel.NewsUiState

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
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        TopBar(
            title = "Details",
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (articleState) {
            is NewsUiState.Loading -> {
                LoadingState(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is NewsUiState.Success -> {
                val article = articleState.articles.firstOrNull()
                article?.let {

                    Texts(
                        text = it.title,
                        modifier = Modifier.padding(bottom = 4.dp),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
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

                    it.newsSite?.let { site ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Texts(
                            text = "Source: $site",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    }
                    DateUtils.formatPublishedDate(it.publishedAt)?.let { date ->
                        Texts(
                            text = "Published: $date",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                        )
                    }

                    it.summary?.let { summary ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Texts(
                            text = summary,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Texts(
                        text = "Read full article",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clickable { uriHandler.openUri(it.url) }
                            .padding(vertical = 4.dp)
                    )
                }
            }

            is NewsUiState.Error -> {
                ErrorState(
                    errorMessage = stringResource(R.string.error_article_details),
                    onRetry = { articleId?.let { viewModel.fetchArticleDetail(it) } },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is NewsUiState.Empty -> {
                EmptyState(
                    message = stringResource(R.string.empty_details),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}