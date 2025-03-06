package com.example.spacenews.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.spacenews.ui.components.WordSearchBar
import com.example.spacenews.viewmodel.SpaceNewsViewModel
import com.example.spacenews.ui.components.LoadingState
import com.example.spacenews.ui.components.NewsItem
import com.example.spacenews.ui.components.Texts

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SpaceNewsViewModel = viewModel()
) {
    // Access state directly
    val searchQuery = viewModel.searchQuery.value
    val newsResults = viewModel.newsArticles
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    var active by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        WordSearchBar(
            searchQuery = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = { active = false },
            active = active,
            onActiveChange = { active = it },
            modifier = Modifier.fillMaxWidth()
        ) {
            when {
                isLoading -> {
                    LoadingState(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                error != null -> {
                    Texts(
                        text = error ?: "Unknown error occurred",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                searchQuery.isNotEmpty() && newsResults.isNotEmpty() -> {
                    LazyColumn {
                        items(newsResults) { article ->
                            NewsItem(
                                article = article,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
                searchQuery.isNotEmpty() && newsResults.isEmpty() -> {
                    Texts(
                        text = "No results found",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                else -> {
                    Texts(
                        text = "Enter a search query",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}