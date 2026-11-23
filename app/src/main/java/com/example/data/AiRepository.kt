package com.example.data

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.GenerativeModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout

class AiRepository {
    private val TAG = "AiRepository"

    private val model: GenerativeModel by lazy {
        FirebaseAI.getInstance(FirebaseApp.getInstance()).generativeModel("gemini-3.8-flash")
    }

    suspend fun generateLiteraryContent(prompt: String): Result<String> {
        return withSafeTimeout {
            val response = model.generateContent(
                "You are an expert multilingual literary assistant and writing coach in World Literature Studio. " +
                        "Maintain the author's original voice and style. Prompt: $prompt"
            )
            response.text ?: "No response generated."
        }
    }

    suspend fun improveText(text: String, task: String): Result<String> {
        val systemPrompt = when (task) {
            "grammar" -> "Correct any grammatical, spelling, or punctuation errors in the following text while preserving its exact literary tone and language: \n\n"
            "vocabulary" -> "Suggest richer, more vivid vocabulary and stylistic enhancements for the following literary text: \n\n"
            "structure" -> "Improve the sentence structure, rhythm, and flow of the following text: \n\n"
            "translate" -> "Translate the following literary text accurately into English (or the requested target language), maintaining poetic resonance: \n\n"
            else -> "Review and refine the following literary text: \n\n"
        }
        return withSafeTimeout {
            val response = model.generateContent(systemPrompt + text)
            response.text ?: "Could not process text."
        }
    }

    private suspend fun <T> withSafeTimeout(block: suspend () -> T): Result<T> {
        return try {
            val result = withTimeout(60_000L) {
                block()
            }
            Result.success(result)
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "AI generation timed out", e)
            Result.failure(Exception("AI request timed out after 60 seconds. Please try again."))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI generation failed", e)
            Result.failure(Exception(e.localizedMessage ?: "AI generation failed."))
        }
    }
}
