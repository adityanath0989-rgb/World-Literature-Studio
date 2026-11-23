package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun AiAssistantScreen(viewModel: MainViewModel) {
    var promptInput by remember { mutableStateOf("") }
    val aiOutput by viewModel.aiOutput.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()
    val scrollState = rememberScrollState()

    val promptSuggestions = listOf(
        "Generate a compelling plot twist for a mystery novel",
        "Suggest lyrical poetry concepts about twilight and memory",
        "Help me outline a historical essay on ancient trade routes",
        "Develop a complex character profile with hidden motives",
        "Explain the narrative theme of existentialism in modern prose"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Gemini AI Literary Coach", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("Your intelligent co-author for world-class literature", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Preset prompts
        Text("Quick Prompts", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        promptSuggestions.forEach { suggestion ->
            OutlinedButton(
                onClick = { promptInput = suggestion },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(suggestion, modifier = Modifier.weight(1f), maxLines = 1)
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = promptInput,
            onValueChange = { promptInput = it },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            placeholder = { Text("Ask your AI literary coach anything...") },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (promptInput.isNotBlank()) {
                    viewModel.runAiPrompt(promptInput)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generate Literary Insight")
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isAiLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (aiOutput.isNotBlank() && !isAiLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Response", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(aiOutput, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
