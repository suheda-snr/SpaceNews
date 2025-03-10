package com.example.spacenews.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spacenews.ui.screens.DetailScreen
import com.example.spacenews.ui.screens.MainScreen
import com.example.spacenews.viewmodel.SpaceNewsViewModel

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