package com.example.spacenews.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.spacenews.viewmodel.SpaceNewsViewModel
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
    val article = articleId?.let { viewModel.getArticleById(it) }
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        if (article != null) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
            ) {
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Image
                article.imageUrl?.let { imageUrl ->
                    Spacer(modifier = Modifier.height(12.dp))
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Article image for ${article.title}",
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
                    article.newsSite?.let {
                        Text(
                            text = "Source: $it",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    formatPublishedDate(article.publishedAt)?.let { formattedDate ->
                        Text(
                            text = "Published: $formattedDate",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                article.summary?.let {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 22.sp
                    )
                }

                // URL
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Read full article",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { uriHandler.openUri(article.url) }
                        .padding(vertical = 4.dp)
                )

                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 20.dp)
                        .width(180.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Back to Home",
                        fontSize = 14.sp
                    )
                }
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Article Not Found",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "The requested article could not be loaded",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}