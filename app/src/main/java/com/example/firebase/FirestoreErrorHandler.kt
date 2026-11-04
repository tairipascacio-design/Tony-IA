package com.example.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestoreException
import org.json.JSONObject

enum class OperationType {
    CREATE,
    READ,
    LIST,
    UPDATE,
    DELETE
}

fun handleFirestoreError(exception: Exception, operation: OperationType, path: String) {
    val errorDetails = JSONObject().apply {
        put("operation", operation.name)
        put("path", path)
        put("timestamp", System.currentTimeMillis())

        if (exception is FirebaseFirestoreException) {
            put("code", exception.code.name)
            put("message", exception.message ?: "No message provided")
        } else {
            put("type", exception.javaClass.simpleName)
            put("message", exception.localizedMessage ?: "Unknown error")
        }
    }

    Log.e("FirestoreError", errorDetails.toString(2), exception)
}
