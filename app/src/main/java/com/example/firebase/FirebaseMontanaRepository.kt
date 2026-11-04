package com.example.firebase

import android.content.Context
import com.example.R
import com.example.data.model.*
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class FirebaseMontanaRepository(private val db: FirebaseFirestore) {

    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        )
    )

    private val auth get() = Firebase.auth

    // Evaluated lazily to prevent early startup crashes before App Check init
    private val geminiModel by lazy {
        Firebase.ai.generativeModel("gemini-3.8-flash")
    }

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("Debes iniciar sesión con Google antes de acceder a la base de datos de Tony Montana.")
    }

    suspend fun saveUserProfile(displayName: String, email: String) {
        val uid = requireUserId()
        val path = "users/$uid"
        val userRef = db.collection("users").document(uid)
        try {
            val payload = mapOf(
                "userId" to uid,
                "displayName" to displayName,
                "email" to email,
                "createdAt" to FieldValue.serverTimestamp(),
                "lastActiveAt" to FieldValue.serverTimestamp()
            )
            userRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.UPDATE, path)
        }
    }

    suspend fun saveSession(session: ChatSession) {
        val uid = requireUserId()
        val path = "users/$uid/sessions/${session.id}"
        val docRef = db.collection("users").document(uid).collection("sessions").document(session.id)
        try {
            val payload = mapOf(
                "id" to session.id,
                "userId" to uid,
                "title" to session.title,
                "lastMessagePreview" to session.lastMessagePreview,
                "messageCount" to session.messageCount,
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
        }
    }

    suspend fun saveMessage(sessionId: String, message: ChatMessage) {
        val uid = requireUserId()
        val path = "users/$uid/sessions/$sessionId/messages/${message.id}"
        val docRef = db.collection("users").document(uid).collection("sessions").document(sessionId).collection("messages").document(message.id)
        try {
            val payload = mapOf(
                "id" to message.id,
                "userId" to uid,
                "sender" to message.sender.name,
                "content" to message.content,
                "toolCalls" to message.toolCalls,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
        }
    }

    suspend fun saveTool(tool: AiTool) {
        val uid = requireUserId()
        val path = "users/$uid/tools/${tool.id}"
        val docRef = db.collection("users").document(uid).collection("tools").document(tool.id)
        try {
            val payload = mapOf(
                "id" to tool.id,
                "userId" to uid,
                "name" to tool.name,
                "tagline" to tool.tagline,
                "purpose" to tool.purpose,
                "category" to tool.category,
                "isEnabled" to tool.isEnabled,
                "executionCount" to tool.executionCount,
                "capabilities" to tool.capabilities,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
        }
    }

    suspend fun saveOptimization(record: OptimizationRecord) {
        val uid = requireUserId()
        val path = "users/$uid/optimizations/${record.id}"
        val docRef = db.collection("users").document(uid).collection("optimizations").document(record.id)
        try {
            val payload = mapOf(
                "id" to record.id,
                "userId" to uid,
                "title" to record.title,
                "module" to record.module,
                "metricBefore" to record.metricBefore,
                "metricAfter" to record.metricAfter,
                "percentageGain" to record.percentageGain,
                "diffSnippet" to record.diffSnippet,
                "explanation" to record.explanation,
                "status" to record.status,
                "createdAt" to FieldValue.serverTimestamp()
            )
            docRef.set(payload).await()
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.CREATE, path)
        }
    }

    suspend fun generateWithGemini(prompt: String): String {
        return withTimeout(60_000L) {
            val response = geminiModel.generateContent(prompt)
            response.text ?: "Tony Montana procesó tu orden en silencio."
        }
    }
}
