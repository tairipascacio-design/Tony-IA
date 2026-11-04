package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AttachmentItem
import com.example.data.model.AttachmentType
import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: ChatMessage,
    onSpeak: (String) -> Unit,
    isSpeakingThis: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timeFormat = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    var copiedRecently by remember { mutableStateOf(false) }

    when (message.sender) {
        MessageSender.SYSTEM -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp)
                    .glassmorphism(
                        shape = RoundedCornerShape(8.dp),
                        backgroundColor = Color(0x3300F0FF),
                        borderColor = NeonCyan.copy(alpha = 0.5f)
                    )
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = message.content,
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        MessageSender.USER -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .glassmorphism(
                            shape = RoundedCornerShape(16.dp, 16.dp, 2.dp, 16.dp),
                            backgroundColor = Color(0xD00D1624),
                            borderColor = NeonCyan.copy(alpha = 0.5f)
                        )
                        .padding(12.dp)
                ) {
                    // Attached media items preview
                    if (message.attachments.isNotEmpty()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            message.attachments.forEach { att ->
                                AttachmentChip(att = att)
                            }
                        }
                    }

                    if (message.content.isNotBlank()) {
                        Text(
                            text = message.content,
                            color = TextPlatinum,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = timeFormat.format(Date(message.timestamp)),
                        color = TextDark,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }

        MessageSender.MONTANA -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .glassmorphism(
                            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 2.dp),
                            backgroundColor = Color(0xE6121722),
                            borderColor = EmpireGold.copy(alpha = 0.6f)
                        )
                        .padding(12.dp)
                ) {
                    // Top Bar: Avatar + Name + Tool Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, EmpireGold, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.montana_avatar_1793582896308),
                                    contentDescription = "Tony",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TONY MONTANA",
                                color = EmpireGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                        }

                        if (isSpeakingThis) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "HABLANDO",
                                    color = NeonCrimson,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                VoiceWaveAnimation()
                            }
                        }
                    }

                    // Tool invocation badges if any
                    if (message.toolCalls.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            message.toolCalls.forEach { tool ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0x3300F0FF))
                                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "⚡ $tool",
                                        color = NeonCyan,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Message Body
                    Text(
                        text = message.content,
                        color = TextPlatinum,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Toolbar: Copy response & Voice Playback
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = timeFormat.format(Date(message.timestamp)),
                            color = TextDark,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Copy Response Button
                            Surface(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Respuesta Montana", message.content)
                                    clipboard.setPrimaryClip(clip)
                                    copiedRecently = true
                                    Toast.makeText(context, "¡Copiado al portapapeles, chico!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(6.dp),
                                color = if (copiedRecently) NeonEmerald.copy(alpha = 0.2f) else Color(0x33233047),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (copiedRecently) NeonEmerald else GlassBorder
                                ),
                                modifier = Modifier.testTag("copy_response_${message.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (copiedRecently) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = "Copiar respuesta",
                                        tint = if (copiedRecently) NeonEmerald else TextMuted,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (copiedRecently) "Copiado" else "Copiar",
                                        color = if (copiedRecently) NeonEmerald else TextMuted,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Tony Montana Voice Audio Button
                            Surface(
                                onClick = { onSpeak(message.content) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSpeakingThis) NeonCrimson.copy(alpha = 0.25f) else Color(0x33233047),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSpeakingThis) NeonCrimson else EmpireGold.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.testTag("speak_voice_${message.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isSpeakingThis) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                        contentDescription = "Voz Tony Montana",
                                        tint = if (isSpeakingThis) NeonCrimson else EmpireGold,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isSpeakingThis) "Silenciar" else "Voz Tony",
                                        color = if (isSpeakingThis) NeonCrimson else EmpireGold,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttachmentChip(att: AttachmentItem) {
    val icon = when (att.type) {
        AttachmentType.IMAGE -> Icons.Default.Image
        AttachmentType.VIDEO -> Icons.Default.VideoLibrary
        AttachmentType.AUDIO -> Icons.Default.Mic
        AttachmentType.CODE -> Icons.Default.Code
        AttachmentType.DOCUMENT -> Icons.Default.Description
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0x4400F0FF))
            .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = NeonCyan,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = att.name,
            color = TextPlatinum,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = att.sizeLabel,
            color = TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
