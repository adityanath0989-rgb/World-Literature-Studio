package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface CurrentScreen {
    data object Home : CurrentScreen
    data object Library : CurrentScreen
    data class Editor(val documentId: String?) : CurrentScreen
    data object BookStudio : CurrentScreen
    data object AiAssistant : CurrentScreen
    data object Statistics : CurrentScreen
    data object Settings : CurrentScreen
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val firestoreRepo = FirestoreRepository()
    private val aiRepo = AiRepository()
    val authManager = AuthManager(application)

    private val _currentScreen = MutableStateFlow<CurrentScreen>(CurrentScreen.Home)
    val currentScreen: StateFlow<CurrentScreen> = _currentScreen.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(SampleData.initialDocuments)
    val documents: StateFlow<List<Document>> = _documents.asStateFlow()

    private val _books = MutableStateFlow<List<Book>>(SampleData.initialBooks)
    val books: StateFlow<List<Book>> = _books.asStateFlow()

    private val _chapters = MutableStateFlow<List<Chapter>>(SampleData.initialChapters)
    val chapters: StateFlow<List<Chapter>> = _chapters.asStateFlow()

    private val _writingGoal = MutableStateFlow(WritingGoal())
    val writingGoal: StateFlow<WritingGoal> = _writingGoal.asStateFlow()

    private val _aiOutput = MutableStateFlow<String>("")
    val aiOutput: StateFlow<String> = _aiOutput.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            val docs = firestoreRepo.getDocuments()
            _documents.value = docs
            val bks = firestoreRepo.getBooks()
            _books.value = bks
        }
    }

    fun navigateTo(screen: CurrentScreen) {
        _currentScreen.value = screen
    }

    fun saveDocument(doc: Document) {
        viewModelScope.launch {
            val updatedDoc = doc.copy(
                wordCount = doc.content.split("\\s+".toRegex()).filter { it.isNotBlank() }.size,
                characterCount = doc.content.length,
                readingTimeMinutes = maxOf(1, doc.content.split("\\s+".toRegex()).size / 200),
                updatedAt = System.currentTimeMillis()
            )
            _documents.update { list ->
                val index = list.indexOfFirst { it.id == updatedDoc.id }
                if (index >= 0) list.toMutableList().apply { set(index, updatedDoc) }
                else list + updatedDoc
            }
            firestoreRepo.saveDocument(updatedDoc)
        }
    }

    fun deleteDocument(docId: String) {
        viewModelScope.launch {
            _documents.update { list -> list.filter { it.id != docId } }
            firestoreRepo.deleteDocument(docId)
        }
    }

    fun runAiPrompt(prompt: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiOutput.value = "Thinking..."
            val result = aiRepo.generateLiteraryContent(prompt)
            _isAiLoading.value = false
            _aiOutput.value = result.getOrElse { "Error: ${it.localizedMessage}" }
        }
    }

    fun improveTextWithAi(text: String, task: String) {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiOutput.value = "Processing text..."
            val result = aiRepo.improveText(text, task)
            _isAiLoading.value = false
            _aiOutput.value = result.getOrElse { "Error: ${it.localizedMessage}" }
        }
    }
}
