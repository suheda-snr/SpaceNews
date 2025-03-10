package com.example.spacenews.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacenews.model.SpaceNewsApi
import com.example.spacenews.model.SpaceNewsArticle
import kotlinx.coroutines.launch

sealed interface NewsUiState {
    data class Success(val articles: List<SpaceNewsArticle>) : NewsUiState
    object Error : NewsUiState
    object Loading : NewsUiState
    object Empty : NewsUiState // For empty search results or no recent articles
}

class SpaceNewsViewModel : ViewModel() {
    var recentNewsUiState: NewsUiState by mutableStateOf(NewsUiState.Loading)
        private set

    var searchNewsUiState: NewsUiState by mutableStateOf(NewsUiState.Empty)
        private set

    var searchQuery = mutableStateOf("")
        private set

    var articleDetailUiState: NewsUiState by mutableStateOf(NewsUiState.Loading) // New state for article details
        private set

    private val api = SpaceNewsApi.getInstance()

    init {
        fetchRecentNews()
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        if (query.isNotEmpty()) {
            fetchNews(query)
        } else {
            searchNewsUiState = NewsUiState.Empty
        }
    }

    private fun fetchNews(query: String) {
        viewModelScope.launch {
            searchNewsUiState = NewsUiState.Loading
            try {
                val response = api.getArticles(query)
                searchNewsUiState = if (response.results.isEmpty()) {
                    NewsUiState.Empty
                } else {
                    NewsUiState.Success(response.results)
                }
                Log.d("SpaceNews", "Fetched articles: ${response.results.map { it.id }}")
            } catch (e: Exception) {
                searchNewsUiState = NewsUiState.Error
                Log.d("ERROR", e.message ?: "Unknown error")
            }
        }
    }

    private fun fetchRecentNews() {
        viewModelScope.launch {
            recentNewsUiState = NewsUiState.Loading
            try {
                val response = api.getRecentArticles()
                recentNewsUiState = if (response.results.isEmpty()) {
                    NewsUiState.Empty
                } else {
                    NewsUiState.Success(response.results)
                }
                response.results.forEach { article ->
                    Log.d("SpaceNews", "Article: ${article.title}, Image URL: ${article.imageUrl}")
                }
            } catch (e: Exception) {
                recentNewsUiState = NewsUiState.Error
                Log.d("ERROR", e.message ?: "Unknown error")
            }
        }
    }

    fun refresh() {
        fetchRecentNews()
        if (searchQuery.value.isNotEmpty()) {
            fetchNews(searchQuery.value)
        }
    }

    fun fetchArticleDetail(articleId: String) {
        viewModelScope.launch {
            articleDetailUiState = NewsUiState.Loading
            try {
                val response = api.getArticleById(articleId)
                articleDetailUiState = if (response != null) {
                    NewsUiState.Success(listOf(response))
                } else {
                    NewsUiState.Empty
                }
                Log.d("SpaceNews", "Fetched article detail: ${response?.title}")
            } catch (e: Exception) {
                articleDetailUiState = NewsUiState.Error
                Log.d("ERROR", e.message ?: "Unknown error")
            }
        }
    }
}