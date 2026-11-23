package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.Document
import com.example.ui.CurrentScreen
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(viewModel: MainViewModel, documentId: String?) {
    val documents by viewModel.documents.collectAsState()
    val context = LocalContext.current

    val document = documents.find { it.id == documentId } ?: Document(
        title = "",
        content = "",
        category = "Creative Literature",
        language = "English"
    )

    var title by remember { mutableStateOf(document.title) }
    var content by remember { mutableStateOf(document.content) }
    var selectedCategory by remember { mutableStateOf(document.category) }
    var selectedLanguage by remember { mutableStateOf(document.language) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showAiModal by remember { mutableStateOf(false) }

    val aiOutput by viewModel.aiOutput.collectAsState()
    val isAiLoading by viewModel.isAiLoading.collectAsState()

    val wordCount = content.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
    val charCount = content.length
    val readingTime = maxOf(1, wordCount / 200)

    // Autosave effect
    LaunchedEffect(title, content, selectedCategory, selectedLanguage) {
        viewModel.saveDocument(
            document.copy(
                title = title,
                content = content,
                category = selectedCategory,
                language = selectedLanguage
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title.ifBlank { "Untitled Writing" }, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.navigateTo(CurrentScreen.Library) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAiModal = true }) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI Assistant", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Default.Share, contentDescription = "Export")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Words: $wordCount", style = MaterialTheme.typography.labelMedium)
                    Text("Chars: $charCount", style = MaterialTheme.typography.labelMedium)
                    Text("~${readingTime} min read", style = MaterialTheme.typography.labelMedium)
                    Badge { Text(selectedLanguage) }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Title of Work...") },
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Formatting Action Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(onClick = { content += "**" }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FormatBold, contentDescription = "Bold", modifier = Modifier.size(18.dp))
                }
                OutlinedButton(onClick = { content += "*" }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FormatItalic, contentDescription = "Italic", modifier = Modifier.size(18.dp))
                }
                OutlinedButton(onClick = { content += "\n> " }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FormatQuote, contentDescription = "Quote", modifier = Modifier.size(18.dp))
                }
                OutlinedButton(onClick = { content += "\n- " }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.FormatListBulleted, contentDescription = "List", modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text("Begin writing your masterpiece here...") },
                shape = RoundedCornerShape(12.dp)
            )
        }
    }

    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export & Publish") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Export your work in professional publishing formats:")
                    Button(onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_SUBJECT, title)
                            putExtra(Intent.EXTRA_TEXT, content)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Export Literature"))
                        showExportDialog = false
                    }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Description, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export as TXT / Markdown")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showExportDialog = false }) { Text("Close") }
            }
        )
    }

    // AI Assistant Modal
    if (showAiModal) {
        AlertDialog(
            onDismissRequest = { showAiModal = false },
            title = { Text("Gemini AI Literary Assistant") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enhance your writing with AI assistance:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.improveTextWithAi(content, "grammar") }, modifier = Modifier.weight(1f)) {
                            Text("Grammar")
                        }
                        Button(onClick = { viewModel.improveTextWithAi(content, "vocabulary") }, modifier = Modifier.weight(1f)) {
                            Text("Vocabulary")
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.improveTextWithAi(content, "structure") }, modifier = Modifier.weight(1f)) {
                            Text("Flow & Tone")
                        }
                        Button(onClick = { viewModel.improveTextWithAi(content, "translate") }, modifier = Modifier.weight(1f)) {
                            Text("Translate")
                        }
                    }
                    if (isAiLoading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                    if (aiOutput.isNotBlank()) {
                        Surface(
                            modifier = Modifier.fillMaxWidth().height(150.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Column(modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState())) {
                                Text(aiOutput, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Button(onClick = {
                            content += "\n\n[AI Revision]:\n$aiOutput"
                            showAiModal = false
                        }) {
                            Text("Insert Suggestion into Text")
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAiModal = false }) { Text("Close") }
            }
        )
    }
}
