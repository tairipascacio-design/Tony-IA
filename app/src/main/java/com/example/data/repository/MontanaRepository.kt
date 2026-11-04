package com.example.data.repository

import com.example.data.model.*
import com.example.data.personality.MontanaPersona
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MontanaRepository(
    private val firebaseRepo: com.example.firebase.FirebaseMontanaRepository? = null,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {
    private val defaultSessionId = "session-1"

    private val _sessions = MutableStateFlow<List<ChatSession>>(
        listOf(
            ChatSession(
                id = defaultSessionId,
                title = "Operación Cyber Miami",
                createdAt = System.currentTimeMillis() - 7200000,
                lastMessagePreview = "The world is yours, chico. Servidores listos.",
                messageCount = 4
            ),
            ChatSession(
                id = "session-2",
                title = "Clúster Digital Ocean",
                createdAt = System.currentTimeMillis() - 86400000,
                lastMessagePreview = "Droplets sincronizados en Frankfurt y NYC.",
                messageCount = 6
            ),
            ChatSession(
                id = "session-3",
                title = "Inspección de Pantalla HUD",
                createdAt = System.currentTimeMillis() - 172800000,
                lastMessagePreview = "Radar cuántico activo a 60 FPS.",
                messageCount = 3
            )
        )
    )
    val sessions: StateFlow<List<ChatSession>> = _sessions.asStateFlow()

    private val _currentSessionId = MutableStateFlow(defaultSessionId)
    val currentSessionId: StateFlow<String> = _currentSessionId.asStateFlow()

    private val sessionMessages = mutableMapOf<String, MutableList<ChatMessage>>()

    init {
        sessionMessages[defaultSessionId] = mutableListOf(
            ChatMessage(
                id = "msg-0",
                sender = MessageSender.SYSTEM,
                content = "MANIFIESTO DEL SISTEMA: Tony Montana AI cargado. Herramientas dinámicas montadas. Stream de pantalla disponible. Modelo Digital Ocean conectado."
            ),
            ChatMessage(
                id = "msg-1",
                sender = MessageSender.MONTANA,
                content = "¡Say hello to my little friend! Bienvenido a mi oficina, socio. Aquí tienes la interfaz más lujosa y pesada de la ciudad. Transmite tu pantalla, arrastra archivos pesados o déjame reorganizar tu código para que vuele. ¿Qué negocio cerramos hoy?",
                isSpoken = false
            )
        )
    }

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        sessionMessages[defaultSessionId]?.toList() ?: emptyList()
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    // Screen Stream State
    private val _screenStream = MutableStateFlow(
        ScreenStreamState(
            isStreaming = false,
            fps = 60,
            bitrateMbps = 4.8f,
            resolution = "1080p @ 60fps",
            latencyMs = 18,
            framesAnalyzed = 1420,
            lastVisionAnalysis = "Radar listo. Activa el stream para escanear tu pantalla en tiempo real."
        )
    )
    val screenStream: StateFlow<ScreenStreamState> = _screenStream.asStateFlow()

    // Dynamic AI Tools
    private val _tools = MutableStateFlow<List<AiTool>>(
        listOf(
            AiTool(
                id = "tool-vision",
                name = "ScreenVision Radar",
                tagline = "Análisis Óptico y OCR en Vivo",
                purpose = "Captura y decodifica fotogramas del stream de pantalla en tiempo real para extraer texto, botones y estados de la UI.",
                capabilities = listOf("Detección de OCR", "Lectura de logs en pantalla", "Inspección de gráficos"),
                category = "Visión",
                isEnabled = true,
                executionCount = 38
            ),
            AiTool(
                id = "tool-refactor",
                name = "CodeRefactor Engine",
                tagline = "Auto-Reorganización de Código",
                purpose = "Reorganiza autónomamente el árbol de componentes, poda recomposiciones redundantes y optimiza el pipeline de render.",
                capabilities = listOf("Análisis AST de Compose", "Eliminación de cuellos de botella", "Poda de jerarquía"),
                category = "Optimización",
                isEnabled = true,
                executionCount = 14
            ),
            AiTool(
                id = "tool-digitalocean",
                name = "Digital Ocean Runner",
                tagline = "Inferencia Remota en Droplet",
                purpose = "Envía prompts y tensores pesados a los servidores privados de Digital Ocean para inferencia sin límites de cuota.",
                capabilities = listOf("Streaming de tokens HTTP", "Balanceo de carga en droplets", "Fallback automático"),
                category = "Cómputo",
                isEnabled = true,
                executionCount = 89
            ),
            AiTool(
                id = "tool-voice",
                name = "Scarface Voice Synth",
                tagline = "Síntesis Vocal Tony Montana",
                purpose = "Modula el motor de Text-To-Speech de Android con cadencia cubana, tono de mando rudo y acento característico de Scarface.",
                capabilities = listOf("Modulación fonética", "Inflexión emocional agresiva", "Frases icónicas instantáneas"),
                category = "Audio",
                isEnabled = true,
                executionCount = 52
            ),
            AiTool(
                id = "tool-files",
                name = "Media Forensic Inspector",
                tagline = "Escáner Profundo de Archivos",
                purpose = "Examina fotos, videos y documentos adjuntos para extraer información confidencial, código y metadatos.",
                capabilities = listOf("Inspección de metadatos EXIF", "Extracción de texto en documentos", "Preprocesamiento multimedia"),
                category = "Archivos",
                isEnabled = true,
                executionCount = 22
            )
        )
    )
    val tools: StateFlow<List<AiTool>> = _tools.asStateFlow()

    // Self-Optimization History
    private val _optimizationHistory = MutableStateFlow<List<OptimizationRecord>>(
        listOf(
            OptimizationRecord(
                id = "opt-1",
                title = "Reorganización de Recomposiciones en LazyColumn",
                timestamp = System.currentTimeMillis() - 3600000,
                module = "ChatFeed / RenderPipeline",
                metricBefore = "42 ms latencia de frame | 118 recomposiciones/s",
                metricAfter = "14 ms latencia de frame | 32 recomposiciones/s",
                percentageGain = "+66% Fluidez",
                diffSnippet = """
                    - LazyColumn(modifier = Modifier.fillMaxSize()) { items(messages) { ... } }
                    + LazyColumn(modifier = Modifier.fillMaxSize(), key = { it.id }) {
                    +   items(items = messages, key = { it.id }, contentType = { it.sender }) { ... }
                    + }
                """.trimIndent(),
                explanation = "Tony identificó recomposiciones fantasmas al recibir tokens de Digital Ocean. Añadió keys estables y contentType para reciclado óptimo de vistas."
            ),
            OptimizationRecord(
                id = "opt-2",
                title = "Compactación de Memoria en Buffer de Pantalla",
                timestamp = System.currentTimeMillis() - 10800000,
                module = "ScreenStream / BitmapCache",
                metricBefore = "184 MB RAM ocupada",
                metricAfter = "98 MB RAM ocupada",
                percentageGain = "-46% Memoria",
                diffSnippet = """
                    - val screenBitmap = Bitmap.createBitmap(w, h, ARGB_8888)
                    + val screenBitmap = inBitmapPool.acquireOrCreate(w, h, HARDWARE)
                """.trimIndent(),
                explanation = "El motor de auto-optimización reconfiguró el búfer del stream a texturas de hardware reutilizables, eliminando pausas de Garbage Collection."
            )
        )
    )
    val optimizationHistory: StateFlow<List<OptimizationRecord>> = _optimizationHistory.asStateFlow()

    // Digital Ocean Config
    private val _digitalOceanConfig = MutableStateFlow(DigitalOceanConfig())
    val digitalOceanConfig: StateFlow<DigitalOceanConfig> = _digitalOceanConfig.asStateFlow()

    // Quote Ticker
    private val _currentQuote = MutableStateFlow(MontanaPersona.FAMOUS_QUOTES.first())
    val currentQuote: StateFlow<String> = _currentQuote.asStateFlow()

    fun rotateQuote() {
        val nextQuote = MontanaPersona.FAMOUS_QUOTES.random()
        _currentQuote.value = nextQuote
    }

    fun switchSession(sessionId: String) {
        _currentSessionId.value = sessionId
        val msgs = sessionMessages.getOrPut(sessionId) {
            mutableListOf(
                ChatMessage(
                    id = UUID.randomUUID().toString(),
                    sender = MessageSender.MONTANA,
                    content = "¡Nueva operación abierta, chico! Aquí Tony Montana reportándose. ¿Qué negocio tienes entre manos?",
                    isSpoken = false
                )
            )
        }
        _messages.value = msgs.toList()
    }

    fun createNewSession(title: String = "Operación ${sessions.value.size + 1}"): String {
        val newId = UUID.randomUUID().toString()
        val newSession = ChatSession(
            id = newId,
            title = title,
            createdAt = System.currentTimeMillis(),
            lastMessagePreview = "Operación iniciada. Montana listo.",
            messageCount = 1
        )
        val updated = listOf(newSession) + _sessions.value
        _sessions.value = updated
        scope.launch {
            try { firebaseRepo?.saveSession(newSession) } catch (_: Exception) {}
        }
        sessionMessages[newId] = mutableListOf(
            ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.MONTANA,
                content = "¡Aquí mando yo! Has creado la sala '$title'. Todos los canales de comunicación y herramientas están asegurados.",
                isSpoken = false
            )
        )
        switchSession(newId)
        return newId
    }

    fun deleteSession(sessionId: String) {
        if (_sessions.value.size <= 1) return
        val updated = _sessions.value.filter { it.id != sessionId }
        _sessions.value = updated
        sessionMessages.remove(sessionId)
        if (_currentSessionId.value == sessionId) {
            switchSession(updated.first().id)
        }
    }

    fun sendMessage(
        content: String,
        attachments: List<AttachmentItem> = emptyList()
    ) {
        val currentId = _currentSessionId.value
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = MessageSender.USER,
            content = content,
            attachments = attachments
        )
        val currentList = sessionMessages.getOrPut(currentId) { mutableListOf() }
        currentList.add(userMsg)
        _messages.value = currentList.toList()
        scope.launch {
            try { firebaseRepo?.saveMessage(currentId, userMsg) } catch (_: Exception) {}
        }

        // Update session preview
        _sessions.value = _sessions.value.map {
            if (it.id == currentId) {
                it.copy(
                    lastMessagePreview = if (content.isNotBlank()) content else "Archivo enviado",
                    messageCount = it.messageCount + 1
                )
            } else it
        }

        // Simulate AI Thinking & Montana Response
        scope.launch {
            delay(650)
            val isStreaming = _screenStream.value.isStreaming
            val activeToolNames = _tools.value.filter { it.isEnabled }.map { it.name }
            val responseText = MontanaPersona.craftMontanaResponse(
                userPrompt = content,
                attachmentsCount = attachments.size,
                isScreenStreaming = isStreaming,
                activeTools = activeToolNames,
                modelName = _digitalOceanConfig.value.modelName
            )

            // If user mentioned optimize or reorganizar, trigger auto optimization
            val lower = content.lowercase()
            val wasOptimizationTriggered = lower.contains("optimiza") || lower.contains("reorganiza") || lower.contains("código")

            val aiMsg = ChatMessage(
                id = UUID.randomUUID().toString(),
                sender = MessageSender.MONTANA,
                content = responseText,
                toolCalls = if (isStreaming) listOf("ScreenVision Radar") else if (wasOptimizationTriggered) listOf("CodeRefactor Engine") else emptyList()
            )
            currentList.add(aiMsg)
            _messages.value = currentList.toList()
            try { firebaseRepo?.saveMessage(currentId, aiMsg) } catch (_: Exception) {}

            _sessions.value = _sessions.value.map {
                if (it.id == currentId) {
                    it.copy(
                        lastMessagePreview = responseText.take(40) + "...",
                        messageCount = it.messageCount + 1
                    )
                } else it
            }

            if (wasOptimizationTriggered) {
                runSelfOptimizationInternal("Auto-Reorganización solicitada por el usuario")
            }
        }
    }

    fun toggleScreenStreaming() {
        val current = _screenStream.value
        val newStreamingState = !current.isStreaming
        _screenStream.value = current.copy(
            isStreaming = newStreamingState,
            lastVisionAnalysis = if (newStreamingState)
                "¡Stream en vivo iniciado! Capturando pantalla a 1080p @ 60 FPS con enlace a Tony Montana."
            else
                "Stream pausado por el usuario. Radar en modo de espera."
        )
    }

    fun analyzeScreenSnapshot() {
        val current = _screenStream.value
        _screenStream.value = current.copy(
            framesAnalyzed = current.framesAnalyzed + 30,
            lastVisionAnalysis = "Fotograma capturado: UI de alto contraste detectada. Nivel de brillo óptimo. Encriptación de datos activa."
        )
        sendMessage("Analiza lo que tengo en mi pantalla ahora mismo, Tony.", emptyList())
    }

    fun toggleTool(toolId: String) {
        _tools.value = _tools.value.map {
            if (it.id == toolId) it.copy(isEnabled = !it.isEnabled) else it
        }
    }

    fun executeTool(toolId: String) {
        scope.launch {
            _tools.value = _tools.value.map {
                if (it.id == toolId) it.copy(isExecuting = true) else it
            }
            delay(1200)
            _tools.value = _tools.value.map {
                if (it.id == toolId) {
                    it.copy(
                        isExecuting = false,
                        executionCount = it.executionCount + 1,
                        lastExecutedAt = System.currentTimeMillis()
                    )
                } else it
            }
        }
    }

    fun addNewCustomTool(name: String, purpose: String, capabilities: List<String>, category: String) {
        val newTool = AiTool(
            id = "custom-${UUID.randomUUID().toString().take(6)}",
            name = name,
            tagline = "Herramienta Dinámica",
            purpose = purpose,
            capabilities = capabilities,
            category = category,
            isEnabled = true,
            executionCount = 0,
            isCustom = true
        )
        _tools.value = _tools.value + newTool
        scope.launch {
            try { firebaseRepo?.saveTool(newTool) } catch (_: Exception) {}
        }
    }

    fun runSelfCodeOptimization() {
        runSelfOptimizationInternal("Ejecución manual desde el panel de optimización")
    }

    private fun runSelfOptimizationInternal(triggerReason: String) {
        scope.launch {
            val record = OptimizationRecord(
                id = "opt-${System.currentTimeMillis()}",
                title = "Reestructuración Dinámica de Interfaz y Enlace Digital Ocean",
                timestamp = System.currentTimeMillis(),
                module = "UI Architecture / ReactiveStateFlow",
                metricBefore = "Frame Drop: 12% | CPU Spike: 38%",
                metricAfter = "Frame Drop: 0.8% | CPU Spike: 9%",
                percentageGain = "+82% Rendimiento",
                diffSnippet = """
                    // Optimización aplicada autónomamente por Tony Montana:
                    - val unoptimizedState = remember { mutableStateOf(heavyFeed) }
                    + val optimizedState by remember { derivedStateOf { heavyFeed.filterOptimized() } }
                    + launchOnVirtualThread(Dispatchers.Default.limitedParallelism(4))
                """.trimIndent(),
                explanation = "Tony Montana reorganizó el árbol de estado para delegar tareas pesadas a corrutinas en segundo plano y reducir la carga de GPU."
            )
            _optimizationHistory.value = listOf(record) + _optimizationHistory.value
            try { firebaseRepo?.saveOptimization(record) } catch (_: Exception) {}
        }
    }

    fun updateDigitalOceanConfig(config: DigitalOceanConfig) {
        _digitalOceanConfig.value = config
    }
}
