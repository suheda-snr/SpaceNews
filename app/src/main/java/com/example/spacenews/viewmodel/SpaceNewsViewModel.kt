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

    var searchQuery = mutableStateOf("")
        private set

    private val api = SpaceNewsApi.getInstance()

    var isLoading = mutableStateOf(false)
        private set

    var error = mutableStateOf<String?>(null)
        private set

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
}