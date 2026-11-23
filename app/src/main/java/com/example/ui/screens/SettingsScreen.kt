package com.example.ui.screens

import android.widget.Toast
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
import com.example.ui.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val user = viewModel.authManager.currentUser
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("Settings & Account", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Cloud Synchronization & Account", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                if (user != null) {
                    Text("Signed in as: ${user.email ?: user.displayName}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = {
                        viewModel.authManager.signOut()
                        Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                        viewModel.loadData()
                    }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                        Text("Sign Out")
                    }
                } else {
                    Text("Sync your literary works across devices with Google Sign-In.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = {
                        coroutineScope.launch {
                            val result = viewModel.authManager.signInWithGoogle()
                            if (result.isSuccess) {
                                Toast.makeText(context, "Signed in successfully!", Toast.LENGTH_SHORT).show()
                                viewModel.loadData()
                            } else {
                                Toast.makeText(context, "Sign-in cancelled or failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign In with Google")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("About World Literature Studio", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Version 1.0 (Production Build)", style = MaterialTheme.typography.bodyMedium)
                Text("Universal multilingual literature, book publishing, and AI writing studio.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            }
        }
    }
}
