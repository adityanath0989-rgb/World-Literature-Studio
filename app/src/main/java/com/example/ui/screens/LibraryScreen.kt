package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Document
import com.example.ui.CurrentScreen
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(viewModel: MainViewModel) {
    val documents by viewModel.documents.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedLanguage by remember { mutableStateOf("All") }

    val categories = listOf("All", "Creative Literature", "Academic & Nonfiction", "Traditional & Regional")
    val languages = listOf("All", "English", "Bengali", "Hindi", "Arabic", "French", "Spanish", "Japanese", "Chinese")

    val filtered = documents.filter { doc ->
        val matchesSearch = doc.title.contains(searchQuery, ignoreCase = true) ||
                doc.content.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || doc.category.equals(selectedCategory, ignoreCase = true)
        val matchesLang = selectedLanguage == "All" || doc.language.equals(selectedLanguage, ignoreCase = true)
        matchesSearch && matchesCategory && matchesLang
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Digital Library", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by title, content, or keywords...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                    }
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Category Filter
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) { cat ->
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text(cat) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Language Filter
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(languages) { lang ->
                FilterChip(
                    selected = selectedLanguage == lang,
                    onClick = { selectedLanguage = lang },
                    label = { Text(lang) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No literary works found", style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered) { doc ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(CurrentScreen.Editor(doc.id)) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(doc.title.ifBlank { "Untitled" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(doc.content, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Badge { Text(doc.language) }
                                    Badge { Text(doc.category) }
                                }
                            }
                            IconButton(onClick = { viewModel.deleteDocument(doc.id) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
