package com.example.data

import java.util.UUID

data class Document(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val category: String = "Creative Literature", // Creative Literature, Academic & Nonfiction, Traditional & Regional
    val subcategory: String = "Short Story",
    val language: String = "English",
    val wordCount: Int = 0,
    val characterCount: Int = 0,
    val readingTimeMinutes: Int = 1,
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isDeleted: Boolean = false,
    val authorId: String = "local_user",
    val authorName: String = "Author"
)

data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val genre: String = "Novel",
    val language: String = "English",
    val targetWordCount: Int = 50000,
    val currentWordCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
    val authorId: String = "local_user"
)

data class Chapter(
    val id: String = UUID.randomUUID().toString(),
    val bookId: String = "",
    val title: String = "",
    val content: String = "",
    val orderIndex: Int = 0,
    val wordCount: Int = 0
)

data class CharacterProfile(
    val id: String = UUID.randomUUID().toString(),
    val bookId: String = "",
    val name: String = "",
    val role: String = "Protagonist",
    val description: String = "",
    val notes: String = ""
)

data class WritingGoal(
    val dailyGoalWords: Int = 500,
    val todayWrittenWords: Int = 120,
    val streakDays: Int = 5,
    val targetTotalWords: Int = 100000
)

data class UserProfile(
    val uid: String = "",
    val displayName: String = "Writer",
    val email: String = "",
    val photoUrl: String = "",
    val preferredLanguage: String = "English",
    val isDarkMode: Boolean = false
)
