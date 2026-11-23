package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.ui.MainViewModel

@Composable
fun BookStudioScreen(viewModel: MainViewModel) {
    val books by viewModel.books.collectAsState()
    val chapters by viewModel.chapters.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Novel & Book Studio", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Button(onClick = { /* Create book action */ }) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Book")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (books.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No active book projects.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(books) { book ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(book.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(book.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(12.dp))

                            LinearProgressIndicator(
                                progress = { (book.currentWordCount.toFloat() / book.targetWordCount.toFloat()).coerceIn(0f, 1f) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Progress: ${book.currentWordCount} / ${book.targetWordCount} words", style = MaterialTheme.typography.labelSmall)
                                Text("${((book.currentWordCount.toFloat() / book.targetWordCount.toFloat()) * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Chapters (${chapters.size}):", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))

                            chapters.forEach { chapter ->
                                Surface(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(chapter.title, style = MaterialTheme.typography.bodyMedium)
                                        Text("${chapter.wordCount} words", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
