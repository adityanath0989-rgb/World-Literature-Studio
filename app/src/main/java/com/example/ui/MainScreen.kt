package com.example.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (currentScreen !is CurrentScreen.Editor) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.Home,
                        onClick = { viewModel.navigateTo(CurrentScreen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.Library,
                        onClick = { viewModel.navigateTo(CurrentScreen.Library) },
                        icon = { Icon(Icons.Default.MenuBook, contentDescription = "Library") },
                        label = { Text("Library") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.BookStudio,
                        onClick = { viewModel.navigateTo(CurrentScreen.BookStudio) },
                        icon = { Icon(Icons.Default.Book, contentDescription = "Studio") },
                        label = { Text("Studio") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.AiAssistant,
                        onClick = { viewModel.navigateTo(CurrentScreen.AiAssistant) },
                        icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI") },
                        label = { Text("AI Coach") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.Statistics,
                        onClick = { viewModel.navigateTo(CurrentScreen.Statistics) },
                        icon = { Icon(Icons.Default.BarChart, contentDescription = "Stats") },
                        label = { Text("Stats") }
                    )
                    NavigationBarItem(
                        selected = currentScreen is CurrentScreen.Settings,
                        onClick = { viewModel.navigateTo(CurrentScreen.Settings) },
                        icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                        label = { Text("Settings") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                is CurrentScreen.Home -> HomeScreen(viewModel)
                is CurrentScreen.Library -> LibraryScreen(viewModel)
                is CurrentScreen.Editor -> EditorScreen(viewModel, screen.documentId)
                is CurrentScreen.BookStudio -> BookStudioScreen(viewModel)
                is CurrentScreen.AiAssistant -> AiAssistantScreen(viewModel)
                is CurrentScreen.Statistics -> StatisticsScreen(viewModel)
                is CurrentScreen.Settings -> SettingsScreen(viewModel)
            }
        }
    }
}
