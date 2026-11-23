package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Document
import com.example.ui.CurrentScreen
import com.example.ui.MainViewModel

@Composable
fun HomeScreen(viewModel: MainViewModel) {
    val documents by viewModel.documents.collectAsState()
    val writingGoal by viewModel.writingGoal.collectAsState()
    val scrollState = rememberScrollState()

    val languages = listOf("All", "English", "Bengali", "Hindi", "Arabic", "French", "Spanish", "Japanese", "Chinese")
    var selectedLang by remember { mutableStateOf("All") }

    val categories = listOf("All Categories", "Creative Literature", "Academic & Nonfiction", "Traditional & Regional")
    var selectedCategory by remember { mutableStateOf("All Categories") }

    val filteredDocs = documents.filter { doc ->
        (selectedLang == "All" || doc.language.equals(selectedLang, ignoreCase = true)) &&
                (selectedCategory == "All Categories" || doc.category.equals(selectedCategory, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Welcome Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "World Literature Studio",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Universal Multilingual Writing & Publishing Sanctuary",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GoalBadge(icon = Icons.Default.Edit, label = "Today's Goal", value = "${writingGoal.todayWrittenWords}/${writingGoal.dailyGoalWords} words")
                    GoalBadge(icon = Icons.Default.LocalFireDepartment, label = "Streak", value = "${writingGoal.streakDays} Days")
                    GoalBadge(icon = Icons.Default.Book, label = "Works", value = "${documents.size}")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.navigateTo(CurrentScreen.Editor(null)) },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Writing")
            }
            OutlinedButton(
                onClick = { viewModel.navigateTo(CurrentScreen.AiAssistant) },
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("AI Coach")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Language Selector
        Text("Writing Language", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { lang ->
                FilterChip(
                    selected = selectedLang == lang,
                    onClick = { selectedLang = lang },
                    label = { Text(lang) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Continue Writing / Recent Works
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Recent Literary Works", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            TextButton(onClick = { viewModel.navigateTo(CurrentScreen.Library) }) {
                Text("View All")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (filteredDocs.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                Text("No works found for selected language or filter.", style = MaterialTheme.typography.bodyMedium)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredDocs.take(4).forEach { doc ->
                    DocumentCard(doc = doc, onClick = { viewModel.navigateTo(CurrentScreen.Editor(doc.id)) })
                }
            }
        }
    }
}

@Composable
fun GoalBadge(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun DocumentCard(doc: Document, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = doc.title.ifBlank { "Untitled Work" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Badge { Text(doc.language) }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = doc.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "${doc.wordCount} words • ~${doc.readingTimeMinutes} min read", style = MaterialTheme.typography.labelSmall)
                Text(text = doc.category, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
