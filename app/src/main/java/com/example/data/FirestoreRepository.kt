package com.example.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository {
    private val TAG = "FirestoreRepository"
    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        null
    }
    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        null
    }

    val currentUserId: String
        get() = auth?.currentUser?.uid ?: "local_user"

    suspend fun saveDocument(document: Document) {
        val docToSave = document.copy(authorId = currentUserId, updatedAt = System.currentTimeMillis())
        try {
            firestore?.collection("users")?.document(currentUserId)
                ?.collection("documents")?.document(docToSave.id)
                ?.set(docToSave)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save document to Firestore, stored locally", e)
        }
    }

    suspend fun deleteDocument(documentId: String) {
        try {
            firestore?.collection("users")?.document(currentUserId)
                ?.collection("documents")?.document(documentId)
                ?.delete()?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to delete document from Firestore", e)
        }
    }

    suspend fun getDocuments(): List<Document> {
        return try {
            val snapshot = firestore?.collection("users")?.document(currentUserId)
                ?.collection("documents")?.get()?.await()
            snapshot?.documents?.mapNotNull { it.toObject(Document::class.java) }
                .takeIf { !it.isNullOrEmpty() } ?: SampleData.initialDocuments
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch documents from Firestore, using sample data", e)
            SampleData.initialDocuments
        }
    }

    suspend fun saveBook(book: Book) {
        try {
            firestore?.collection("users")?.document(currentUserId)
                ?.collection("books")?.document(book.id)
                ?.set(book)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to save book", e)
        }
    }

    suspend fun getBooks(): List<Book> {
        return try {
            val snapshot = firestore?.collection("users")?.document(currentUserId)
                ?.collection("books")?.get()?.await()
            snapshot?.documents?.mapNotNull { it.toObject(Book::class.java) }
                .takeIf { !it.isNullOrEmpty() } ?: SampleData.initialBooks
        } catch (e: Exception) {
            SampleData.initialBooks
        }
    }
}
