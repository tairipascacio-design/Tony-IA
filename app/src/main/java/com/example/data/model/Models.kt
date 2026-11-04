package com.example.data.model

enum class MessageSender {
    USER,
    MONTANA,
    SYSTEM
}

enum class AttachmentType {
    IMAGE,
    VIDEO,
    DOCUMENT,
    CODE,
    AUDIO
}

data class AttachmentItem(
    val id: String,
    val name: String,
    val type: AttachmentType,
    val uriString: String? = null,
    val sizeLabel: String = "1.2 MB",
    val previewDescription: String = "Archivo cargado"
)

data class ChatMessage(
    val id: String,
    val sender: MessageSender,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val attachments: List<AttachmentItem> = emptyList(),
    val toolCalls: List<String> = emptyList(),
    val codeSnippet: String? = null,
    val audioDurationSeconds: Int? = null,
    val isSpoken: Boolean = false
)

data class ChatSession(
    val id: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastMessagePreview: String,
    val messageCount: Int
)

data class AiTool(
    val id: String,
    val name: String,
    val tagline: String,
    val purpose: String,
    val capabilities: List<String>,
    val category: String,
    val isEnabled: Boolean = true,
    val executionCount: Int = 0,
    val lastExecutedAt: Long? = null,
    val isExecuting: Boolean = false,
    val isCustom: Boolean = false
)

data class OptimizationRecord(
    val id: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis(),
    val module: String,
    val metricBefore: String,
    val metricAfter: String,
    val percentageGain: String,
    val diffSnippet: String,
    val explanation: String,
    val status: String = "APLICADO"
)

data class ScreenStreamState(
    val isStreaming: Boolean = false,
    val fps: Int = 60,
    val bitrateMbps: Float = 4.8f,
    val resolution: String = "1080p @ 60fps",
    val latencyMs: Int = 18,
    val framesAnalyzed: Int = 0,
    val lastVisionAnalysis: String = "Esperando transmisión en vivo..."
)

data class DigitalOceanConfig(
    val endpointUrl: String = "https://montana-ai-do-cluster.ondigitalocean.app/v1/chat/completions",
    val apiKey: String = "",
    val modelName: String = "llama-3.3-70b-instruct",
    val temperature: Float = 0.75f,
    val maxTokens: Int = 2048,
    val isConnected: Boolean = true
)
