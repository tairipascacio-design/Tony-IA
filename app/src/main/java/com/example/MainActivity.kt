package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChatMessage
import com.example.data.personality.Soundbite
import com.example.data.repository.MontanaRepository
import com.example.ui.components.*
import com.example.ui.theme.*
import kotlinx.coroutines.launch

import com.example.firebase.configureAppCheck
import com.example.firebase.AuthManager
import com.example.firebase.FirebaseMontanaRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.credentials.CredentialManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure App Check before setContent, ViewModels or Firebase operations
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }

                DisposableEffect(Unit) {
                    val listener = FirebaseAuth.AuthStateListener { auth ->
                        currentUser = auth.currentUser
                    }
                    Firebase.auth.addAuthStateListener(listener)
                    onDispose {
                        Firebase.auth.removeAuthStateListener(listener)
                    }
                }

                if (currentUser == null) {
                    AuthScreen(onAuthSuccess = { currentUser = Firebase.auth.currentUser })
                } else {
                    MontanaApp(
                        currentUserId = currentUser!!.uid,
                        userEmail = currentUser?.email ?: "socio@montana.ai"
                    )
                }
            }
        }
    }
}

@Composable
fun MontanaApp(
    currentUserId: String,
    userEmail: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember { CredentialManager.create(context) }
    val firebaseRepo = remember { FirebaseMontanaRepository(context) }
    val repository = remember { MontanaRepository(firebaseRepo = firebaseRepo) }
    val voiceSpeaker = remember { MontanaVoiceSpeaker(context) }

    DisposableEffect(Unit) {
        onDispose {
            voiceSpeaker.release()
        }
    }

    // Repository states
    val sessions by repository.sessions.collectAsState()
    val currentSessionId by repository.currentSessionId.collectAsState()
    val messages by repository.messages.collectAsState()
    val screenStream by repository.screenStream.collectAsState()
    val tools by repository.tools.collectAsState()
    val optimizationHistory by repository.optimizationHistory.collectAsState()
    val digitalOceanConfig by repository.digitalOceanConfig.collectAsState()
    val currentQuote by repository.currentQuote.collectAsState()
    val isSpeaking by voiceSpeaker.isSpeaking.collectAsState()

    // UI state toggles
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var showToolsPanel by remember { mutableStateOf(false) }
    var showOptimizationSheet by remember { mutableStateOf(false) }
    var showDigitalOceanConfig by remember { mutableStateOf(false) }
    var showCapabilitiesManifest by remember { mutableStateOf(false) }
    var showScreenHud by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    // Handle back button when drawer is open
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ChatDrawerContent(
                sessions = sessions,
                currentSessionId = currentSessionId,
                onSelectSession = { id -> repository.switchSession(id) },
                onNewChat = { repository.createNewSession() },
                onDeleteSession = { id -> repository.deleteSession(id) },
                digitalOceanConfig = digitalOceanConfig,
                onOpenDigitalOceanConfig = { showDigitalOceanConfig = true },
                onPlaySoundbite = { soundbite: Soundbite ->
                    voiceSpeaker.speakMontana(soundbite.text)
                },
                onOpenCapabilitiesManifest = { showCapabilitiesManifest = true },
                userEmail = userEmail,
                onSignOut = {
                    AuthManager.signOut(
                        context = context,
                        credentialManager = credentialManager,
                        onSignOutComplete = {
                            scope.launch { drawerState.close() }
                        },
                        scope = scope
                    )
                },
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(ObsidianBlack),
            topBar = {
                MontanaHeader(
                    currentQuote = currentQuote,
                    onQuoteClick = { repository.rotateQuote() },
                    isStreaming = screenStream.isStreaming,
                    onToggleStream = {
                        repository.toggleScreenStreaming()
                        showScreenHud = true
                    },
                    isSpeaking = isSpeaking,
                    activeToolsCount = tools.count { it.isEnabled },
                    onOpenToolsPanel = { showToolsPanel = true },
                    onOpenOptimizationSheet = { showOptimizationSheet = true },
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    modelName = digitalOceanConfig.modelName
                )
            },
            bottomBar = {
                ChatInputBar(
                    onSendMessage = { text, attachments ->
                        repository.sendMessage(text, attachments)
                    },
                    isStreaming = screenStream.isStreaming,
                    onToggleStream = {
                        repository.toggleScreenStreaming()
                        showScreenHud = !showScreenHud
                    }
                )
            },
            containerColor = ObsidianBlack
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ObsidianBlack,
                                ObsidianDarkSurface,
                                Color(0xFF090D14)
                            )
                        )
                    )
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Optional Live Screen Stream HUD banner if streaming or expanded
                    AnimatedVisibility(visible = showScreenHud || screenStream.isStreaming) {
                        ScreenShareHud(
                            streamState = screenStream,
                            onToggleStream = { repository.toggleScreenStreaming() },
                            onCaptureAndAnalyze = { repository.analyzeScreenSnapshot() }
                        )
                    }

                    // Chat Messages Feed
                    if (messages.isEmpty()) {
                        EmptyChatState(
                            onQuickPrompt = { prompt ->
                                repository.sendMessage(prompt, emptyList())
                            }
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .testTag("chat_messages_feed"),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            items(messages, key = { it.id }) { msg ->
                                MessageBubble(
                                    message = msg,
                                    onSpeak = { text ->
                                        if (isSpeaking) {
                                            voiceSpeaker.stop()
                                        } else {
                                            voiceSpeaker.speakMontana(text)
                                        }
                                    },
                                    isSpeakingThis = isSpeaking && voiceSpeaker.currentSpokenText.value.contains(msg.content.take(15))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Expandable AI Tools Panel
    if (showToolsPanel) {
        AiToolsPanel(
            tools = tools,
            onToggleTool = { toolId -> repository.toggleTool(toolId) },
            onExecuteTool = { toolId -> repository.executeTool(toolId) },
            onAddNewTool = { name, purpose, capabilities, category ->
                repository.addNewCustomTool(name, purpose, capabilities, category)
            },
            onDismiss = { showToolsPanel = false }
        )
    }

    // Self-Optimization & Code Reorganization Sheet
    if (showOptimizationSheet) {
        OptimizationHistorySheet(
            history = optimizationHistory,
            onTriggerOptimization = { repository.runSelfCodeOptimization() },
            onDismiss = { showOptimizationSheet = false }
        )
    }

    // Digital Ocean Config Dialog
    if (showDigitalOceanConfig) {
        DigitalOceanConfigDialog(
            currentConfig = digitalOceanConfig,
            onSave = { updated -> repository.updateDigitalOceanConfig(updated) },
            onDismiss = { showDigitalOceanConfig = false }
        )
    }

    // AI Capabilities Manifest Dialog
    if (showCapabilitiesManifest) {
        CapabilitiesManifestDialog(
            onDismiss = { showCapabilitiesManifest = false }
        )
    }
}

@Composable
fun EmptyChatState(onQuickPrompt: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .border(2.dp, EmpireGold, CircleShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.montana_avatar_1793582896308),
                contentDescription = "Tony Montana",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "THE WORLD IS YOURS",
            color = EmpireGold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Interfaz Cyberpunk Ejecutiva para Inteligencia Artificial. Transmite tu pantalla, arrastra archivos y deja que Tony reorganice el código.",
            color = TextMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 17.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Action Chips
        val quickPrompts = listOf(
            "Transmite mi pantalla y analiza la UI",
            "Reorganiza y optimiza el código de la app",
            "¿Cómo configuro mi droplet de Digital Ocean?",
            "Say hello to my little friend!"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            quickPrompts.forEach { prompt ->
                Surface(
                    onClick = { onQuickPrompt(prompt) },
                    shape = RoundedCornerShape(10.dp),
                    color = GlassCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = EmpireGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = prompt,
                            color = TextPlatinum,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
