package com.example.spacenews.model

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class SpaceNewsArticle(
    val id: String,
    val title: String,
    val summary: String?,
    val url: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("news_site") val newsSite: String?,
    @SerializedName("published_at") val publishedAt: String?
)

data class SpaceNewsResponse(
    val results: List<SpaceNewsArticle>
)

const val SPACE_NEWS_BASE_URL = "https://api.spaceflightnewsapi.net/v4/"

interface SpaceNewsApi {
    @GET("articles/")
    suspend fun getArticles(@Query("title_contains") query: String): SpaceNewsResponse

    @GET("articles/?_limit=10&_sort=published_at:desc")
    suspend fun getRecentArticles(): SpaceNewsResponse

    @GET("articles/{id}")
    suspend fun getArticleById(@Path("id") articleId: String): SpaceNewsArticle

    companion object {
        var service: SpaceNewsApi? = null

        fun getInstance(): SpaceNewsApi {
            if (service == null) {
                service = Retrofit.Builder()
                    .baseUrl(SPACE_NEWS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SpaceNewsApi::class.java)
            }
            return service!!
        }
    }
}