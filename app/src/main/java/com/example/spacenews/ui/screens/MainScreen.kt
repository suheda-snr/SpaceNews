package com.example.spacenews.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.spacenews.R
import com.example.spacenews.ui.components.EmptyState
import com.example.spacenews.ui.components.ErrorState
import com.example.spacenews.ui.components.LoadingState
import com.example.spacenews.ui.components.NewsCard
import com.example.spacenews.ui.components.NewsItem
import com.example.spacenews.ui.components.Texts
import com.example.spacenews.ui.components.WordSearchBar
import com.example.spacenews.viewmodel.NewsUiState
import com.example.spacenews.viewmodel.SpaceNewsViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SpaceNewsViewModel = viewModel(),
    navController: NavController
) {
    val searchQuery = viewModel.searchQuery.value
    val searchUiState = viewModel.searchNewsUiState
    val recentUiState = viewModel.recentNewsUiState
    var active by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxSize()) {
        WordSearchBar(
            searchQuery = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = {
                active = true
                focusManager.clearFocus()
            },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            when (searchUiState) {
                is NewsUiState.Loading -> LoadingState(modifier = Modifier.align(Alignment.CenterHorizontally))
                is NewsUiState.Error -> ErrorState(
                    errorMessage = stringResource(R.string.unknown_error),
                    onRetry = { viewModel.refresh() }
                )
                is NewsUiState.Success -> LazyColumn {
                    items(searchUiState.articles) { article ->
                        NewsItem(
                            article = article,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    navController.navigate("detail_screen/${article.id}")
                                }
                        )
                    }
                }
                is NewsUiState.Empty -> {
                    if (searchQuery.isNotEmpty()) {
                        EmptyState(message = stringResource(R.string.no_results))
                    } else {
                        EmptyState(message = stringResource(R.string.search))
                    }
                }
            }
        }

        Texts(
            text = stringResource(R.string.latest_news),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        when (recentUiState) {
            is NewsUiState.Loading -> LoadingState(modifier = Modifier.align(Alignment.CenterHorizontally))
            is NewsUiState.Error -> ErrorState(
                errorMessage = stringResource(R.string.unknown_error),
                onRetry = { viewModel.refresh() }
            )
            is NewsUiState.Success -> LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentUiState.articles) { article ->
                    NewsCard(
                        title = article.title,
                        imageUrl = article.imageUrl,
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                navController.navigate("detail_screen/${article.id}")
                            }
                    )
                }
            }
            is NewsUiState.Empty -> EmptyState(message = stringResource(R.string.no_recent_articles))
        }
    }
}