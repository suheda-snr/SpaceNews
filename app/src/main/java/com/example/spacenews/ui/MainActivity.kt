package com.example.spacenews.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.spacenews.ui.screens.MainScreen
import com.example.spacenews.ui.theme.WordScopeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordScopeTheme {
                MainScreen()
            }
        }
    }
}
