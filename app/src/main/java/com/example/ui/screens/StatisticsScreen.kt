package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel

@Composable
fun StatisticsScreen(viewModel: MainViewModel) {
    val documents by viewModel.documents.collectAsState()
    val writingGoal by viewModel.writingGoal.collectAsState()
    val scrollState = rememberScrollState()

    val totalWords = documents.sumOf { it.wordCount }
    val totalChars = documents.sumOf { it.characterCount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Writing Statistics", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), title = "Total Words", value = "$totalWords", icon = Icons.Default.Edit)
            StatCard(modifier = Modifier.weight(1f), title = "Documents", value = "${documents.size}", icon = Icons.Default.Description)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(modifier = Modifier.weight(1f), title = "Characters", value = "$totalChars", icon = Icons.Default.TextFormat)
            StatCard(modifier = Modifier.weight(1f), title = "Streak", value = "${writingGoal.streakDays} Days", icon = Icons.Default.LocalFireDepartment)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Daily Writing Goal", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { (writingGoal.todayWrittenWords.toFloat() / writingGoal.dailyGoalWords.toFloat()).coerceIn(0f, 1f) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("${writingGoal.todayWrittenWords} of ${writingGoal.dailyGoalWords} words written today", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f))
        }
    }
}
