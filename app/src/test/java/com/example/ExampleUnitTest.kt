package com.example

import com.example.data.model.AttachmentItem
import com.example.data.model.AttachmentType
import com.example.data.personality.MontanaPersona
import com.example.data.repository.MontanaRepository
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testMontanaPersonaGreeting() {
        val response = MontanaPersona.craftMontanaResponse(userPrompt = "Hola Tony")
        assertTrue(response.contains("Tony Montana") || response.contains("chico") || response.contains("asere"))
        assertTrue(response.contains("world is yours") || response.contains("little friend") || response.contains("balls"))
    }

    @Test
    fun testMontanaPersonaScreenStreamDetection() {
        val response = MontanaPersona.craftMontanaResponse(
            userPrompt = "Qué opinas de mi interfaz",
            isScreenStreaming = true
        )
        assertTrue(response.contains("pantalla") || response.contains("stream") || response.contains("radar"))
    }

    @Test
    fun testMontanaPersonaOptimizationTrigger() {
        val response = MontanaPersona.craftMontanaResponse(
            userPrompt = "Optimiza y reorganiza el código de la app"
        )
        assertTrue(response.contains("Auto-Reorganización") || response.contains("código"))
    }

    @Test
    fun testRepositoryToolsAndSessions() {
        val repo = MontanaRepository()
        val initialSessionsCount = repo.sessions.value.size
        val newSessionId = repo.createNewSession("Operación Test")
        assertEquals(initialSessionsCount + 1, repo.sessions.value.size)
        assertEquals(newSessionId, repo.currentSessionId.value)

        // Test tool toggle
        val tool = repo.tools.value.first()
        val initialEnabled = tool.isEnabled
        repo.toggleTool(tool.id)
        val toggled = repo.tools.value.first { it.id == tool.id }
        assertEquals(!initialEnabled, toggled.isEnabled)

        // Test custom tool addition
        repo.addNewCustomTool("TestTool", "Test purpose", listOf("Cap1", "Cap2"), "Cómputo")
        val found = repo.tools.value.any { it.name == "TestTool" }
        assertTrue(found)
    }
}
