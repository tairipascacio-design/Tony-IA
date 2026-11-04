package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.ChatSession
import com.example.firebase.FirebaseMontanaRepository
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Test

class MontanaFirebaseRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun saveSession_authenticatedUser_succeeds() = runBlocking {
    signInTestUser("alice@montana.test")
    val repo = FirebaseMontanaRepository(firestore)
    val session = ChatSession(
      id = "sess_${System.currentTimeMillis()}",
      title = "Operación Alfa",
      lastMessagePreview = "Probando",
      messageCount = 1
    )

    withTimeout(5000L) {
      repo.saveSession(session)
    }
  }

  @Test
  fun saveSession_unauthenticatedUser_throwsSecurityException() = runBlocking {
    auth.signOut()
    val repo = FirebaseMontanaRepository(firestore)
    val session = ChatSession(
      id = "sess_${System.currentTimeMillis()}",
      title = "Operación Fallida",
      lastMessagePreview = "Sin auth",
      messageCount = 0
    )

    try {
      withTimeout(3000L) {
        repo.saveSession(session)
      }
      fail("Expected IllegalStateException for unauthenticated user")
    } catch (e: IllegalStateException) {
      // Expected fail-fast
      assertNotNull(e.message)
    }
  }

  @Test
  fun directFirestoreWrite_crossUser_failsWithPermissionDenied() = runBlocking {
    val aliceUid = signInTestUser("alice@montana.test")
    signInTestUser("bob@montana.test")

    // Bob attempts to write directly into Alice's sessions collection
    val docRef = firestore.collection("users").document(aliceUid).collection("sessions").document("hacked_session")
    try {
      withTimeout(3000L) {
        docRef.set(mapOf<String, Any>("title" to "Hacked", "userId" to aliceUid)).await()
      }
      fail("Expected PERMISSION_DENIED on cross-user write")
    } catch (e: Exception) {
      val firestoreEx = e.unwrapFirestoreException()
      if (firestoreEx != null) {
        org.junit.Assert.assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreEx.code)
      }
    }
  }
}
