package com.example.spacenews.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacenews.model.SpaceNewsApi
import com.example.spacenews.model.SpaceNewsArticle
import kotlinx.coroutines.launch

class SpaceNewsViewModel : ViewModel() {
    var newsArticles = mutableStateListOf<SpaceNewsArticle>()
        private set

    var recentArticles = mutableStateListOf<SpaceNewsArticle>() // For recent articles
        private set

    var searchQuery = mutableStateOf("")
        private set

    private val api = SpaceNewsApi.getInstance()

    var isLoading = mutableStateOf(false) // For search
        private set

    var isLoadingRecent = mutableStateOf(false) // For recent articles
        private set

    var error = mutableStateOf<String?>(null)
        private set

    // Load recent articles on init
    init {
        fetchRecentNews()
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
        if (query.isNotEmpty()) {
            fetchNews(query)
        } else {
            newsArticles.clear()
        }
    }

    private fun fetchNews(query: String) {
        viewModelScope.launch {
            isLoading.value = true
            error.value = null
            try {
                val response = api.getArticles(query)
                newsArticles.clear()
                newsArticles.addAll(response.results)
            } catch (e: Exception) {
                error.value = e.message.toString()
                Log.d("ERROR", error.value ?: "Unknown error")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun fetchRecentNews() {
        viewModelScope.launch {
            isLoadingRecent.value = true
            error.value = null
            try {
                val response = api.getRecentArticles()
                recentArticles.clear()
                response.results.forEach { article ->
                    Log.d("SpaceNews", "Article: ${article.title}, Image URL: ${article.imageUrl}")
                }
                recentArticles.addAll(response.results)
            } catch (e: Exception) {
                error.value = e.message.toString()
                Log.d("ERROR", error.value ?: "Unknown error")
            } finally {
                isLoadingRecent.value = false
            }
        }
    }

    fun refresh() {
        fetchRecentNews()
        if (searchQuery.value.isNotEmpty()) {
            fetchNews(searchQuery.value)
        }
    }
}