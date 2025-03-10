package com.example.spacenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spacenews.ui.theme.SpaceNewsTheme
import com.example.spacenews.ui.screens.MainScreen
import com.example.spacenews.ui.screens.DetailScreen
import com.example.spacenews.viewmodel.SpaceNewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpaceNewsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavigationApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: SpaceNewsViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable(route = "home") {
            MainScreen(navController = navController, modifier = modifier, viewModel = viewModel)
        }

        composable(
            route = "detail_screen/{articleId}",
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId")
            DetailScreen(navController = navController, modifier = modifier, articleId = articleId, viewModel = viewModel)
        }
    }
}
