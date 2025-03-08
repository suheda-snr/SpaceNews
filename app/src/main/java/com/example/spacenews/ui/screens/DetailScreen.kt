package com.example.spacenews.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DetailScreen(
    articleId: String?,
    modifier: Modifier = Modifier,
    navController: NavController
) {

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Detail Screen for article ID: $articleId",
            modifier = modifier.padding(16.dp)
        )

        Button (
            onClick = { navController.navigate("home")}
        ) {
            Text("Back to home")
        }
    }
}