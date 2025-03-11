package com.example.spacenews.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.spacenews.R
import com.example.spacenews.ui.components.ArticleContent
import com.example.spacenews.ui.components.ArticleHeader
import com.example.spacenews.ui.components.EmptyState
import com.example.spacenews.ui.components.ErrorState
import com.example.spacenews.ui.components.LoadingState
import com.example.spacenews.ui.components.SourceAndDate
import com.example.spacenews.ui.components.TopBar
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
            is NewsUiState.Loading -> LoadingState(modifier = Modifier.align(Alignment.CenterHorizontally))
            is NewsUiState.Success -> {
                val article = articleState.articles.firstOrNull()
                article?.let {
                    ArticleHeader(title = it.title, imageUrl = it.imageUrl)
                    SourceAndDate(newsSite = it.newsSite, publishedAt = it.publishedAt)
                    ArticleContent(summary = it.summary, url = it.url)
                }
            }
            is NewsUiState.Error -> ErrorState(
                errorMessage = stringResource(R.string.error_article_details),
                onRetry = { articleId?.let { viewModel.fetchArticleDetail(it) } },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            is NewsUiState.Empty -> EmptyState(
                message = stringResource(R.string.empty_details),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}