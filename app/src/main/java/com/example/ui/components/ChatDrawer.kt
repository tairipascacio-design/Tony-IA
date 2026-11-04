package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ChatSession
import com.example.data.model.DigitalOceanConfig
import com.example.data.personality.MontanaPersona
import com.example.data.personality.Soundbite
import com.example.ui.theme.*

@Composable
fun ChatDrawerContent(
    sessions: List<ChatSession>,
    currentSessionId: String,
    onSelectSession: (String) -> Unit,
    onNewChat: () -> Unit,
    onDeleteSession: (String) -> Unit,
    digitalOceanConfig: DigitalOceanConfig,
    onOpenDigitalOceanConfig: () -> Unit,
    onPlaySoundbite: (Soundbite) -> Unit,
    onOpenCapabilitiesManifest: () -> Unit,
    userEmail: String = "socio@montana.ai",
    onSignOut: () -> Unit = {},
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = ObsidianBlack,
        modifier = Modifier.width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            ObsidianBlack,
                            ObsidianDarkSurface,
                            Color(0xFF090D14)
                        )
                    )
                )
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Hero Brand
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.5.dp, EmpireGold, RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.montana_avatar_1793582896308),
                        contentDescription = "Tony Montana",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "MONTANA AI",
                        color = EmpireGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "THE WORLD IS YOURS",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // New Chat Button
            Button(
                onClick = {
                    onNewChat()
                    onCloseDrawer()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmpireGold,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("new_chat_button")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "NUEVA OPERACIÓN / CHAT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "HISTORIAL DE CONVERSACIONES:",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Chat Sessions List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sessions, key = { it.id }) { session ->
                    val isSelected = session.id == currentSessionId
                    Surface(
                        onClick = {
                            onSelectSession(session.id)
                            onCloseDrawer()
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) GlassCard else Color(0x33101824),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) EmpireGold.copy(alpha = 0.8f) else Color(0x22FFFFFF)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("session_item_${session.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = session.title,
                                    color = if (isSelected) EmpireGold else TextPlatinum,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = session.lastMessagePreview,
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (sessions.size > 1) {
                                IconButton(
                                    onClick = { onDeleteSession(session.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Eliminar chat",
                                        tint = TextDark,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "FRASES CLAVE (VOZ MONTANA):",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    MontanaPersona.TONY_SOUNDBITES.forEach { soundbite ->
                        Surface(
                            onClick = { onPlaySoundbite(soundbite) },
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x33171322),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCrimson.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = null,
                                    tint = NeonCrimson,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = soundbite.title,
                                    color = TextPlatinum,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "DISPARAR",
                                    color = NeonCrimson,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Digital Ocean Status Pill & Config Action
            Surface(
                onClick = onOpenDigitalOceanConfig,
                shape = RoundedCornerShape(12.dp),
                color = Color(0xDD0C1320),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("digital_ocean_settings_btn")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(NeonEmerald)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "MODELO DIGITAL OCEAN",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = digitalOceanConfig.modelName,
                            color = TextMuted,
                            fontSize = 10.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // AI Capabilities Matrix Button
            TextButton(
                onClick = onOpenCapabilitiesManifest,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = EmpireGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Manifiesto de Poder de la IA",
                    color = EmpireGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // User Info & Sign Out Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassmorphism(
                        shape = RoundedCornerShape(10.dp),
                        backgroundColor = Color(0x66101824),
                        borderColor = Color(0x22FFFFFF)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "SESIÓN GOOGLE",
                        color = NeonCyan,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = userEmail,
                        color = TextMuted,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(
                    onClick = onSignOut,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Cerrar sesión",
                        tint = NeonCrimson,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
