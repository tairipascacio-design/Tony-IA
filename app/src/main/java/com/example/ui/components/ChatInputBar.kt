package com.example.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttachmentItem
import com.example.data.model.AttachmentType
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun ChatInputBar(
    onSendMessage: (String, List<AttachmentItem>) -> Unit,
    isStreaming: Boolean,
    onToggleStream: () -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf<List<AttachmentItem>>(emptyList()) }
    var showAttachmentPickerMenu by remember { mutableStateOf(false) }

    // System Photo/Video Picker (Zero-permission Android Photo Picker)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val isVideo = uri.toString().contains("video")
            val newAttachment = AttachmentItem(
                id = UUID.randomUUID().toString(),
                name = if (isVideo) "video_capturado.mp4" else "evidencia_foto.jpg",
                type = if (isVideo) AttachmentType.VIDEO else AttachmentType.IMAGE,
                uriString = uri.toString(),
                sizeLabel = if (isVideo) "8.4 MB" else "2.1 MB"
            )
            attachments = attachments + newAttachment
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x0007090E),
                        ObsidianBlack.copy(alpha = 0.95f),
                        ObsidianBlack
                    )
                )
            )
            .navigationBarsPadding()
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        // Pending attachments preview row
        AnimatedVisibility(visible = attachments.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(attachments, key = { it.id }) { att ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xCC1A2333),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (att.type == AttachmentType.VIDEO) Icons.Default.VideoLibrary else Icons.Default.Image,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = att.name,
                                color = TextPlatinum,
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = { attachments = attachments.filter { it.id != att.id } },
                                modifier = Modifier.size(18.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Quitar archivo",
                                    tint = NeonCrimson,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Input Bar with Glassmorphism
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .glassmorphism(
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = Color(0xE0131A26),
                    borderColor = GlassBorderGold
                )
                .padding(horizontal = 6.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment Button
            IconButton(
                onClick = { showAttachmentPickerMenu = true },
                modifier = Modifier
                    .size(40.dp)
                    .testTag("attach_file_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Adjuntar archivo, foto o video",
                    tint = EmpireGold,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Screen Share quick toggle
            IconButton(
                onClick = onToggleStream,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("quick_stream_toggle_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ScreenShare,
                    contentDescription = "Transmitir pantalla",
                    tint = if (isStreaming) NeonCrimson else TextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Text Input Field
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        text = "Habla con Tony Montana...",
                        color = TextDark,
                        fontSize = 13.sp
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = TextPlatinum,
                    unfocusedTextColor = TextPlatinum
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_text_field"),
                maxLines = 4
            )

            // Send Button with Gold Gradient
            IconButton(
                onClick = {
                    if (text.isNotBlank() || attachments.isNotEmpty()) {
                        onSendMessage(text.trim(), attachments)
                        text = ""
                        attachments = emptyList()
                    }
                },
                enabled = text.isNotBlank() || attachments.isNotEmpty(),
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        if (text.isNotBlank() || attachments.isNotEmpty())
                            Brush.linearGradient(listOf(EmpireGold, EmpireGoldDark))
                        else
                            Brush.linearGradient(listOf(Color(0xFF222B38), Color(0xFF161E28)))
                    )
                    .testTag("send_message_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Enviar orden",
                    tint = if (text.isNotBlank() || attachments.isNotEmpty()) ObsidianBlack else TextDark,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    // Attachment Source Selector Menu
    if (showAttachmentPickerMenu) {
        AlertDialog(
            onDismissRequest = { showAttachmentPickerMenu = false },
            containerColor = ObsidianDarkSurface,
            title = {
                Text(
                    text = "CARGAR MATERIAL AL SISTEMA",
                    color = EmpireGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Selecciona el tipo de archivo que quieres que Tony inspeccione:",
                        color = TextMuted,
                        fontSize = 11.sp
                    )

                    // Option 1: Gallery Photos / Videos (Photo Picker)
                    Surface(
                        onClick = {
                            showAttachmentPickerMenu = false
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = GlassCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, tint = NeonCyan)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Fotos y Videos del Dispositivo", color = TextPlatinum, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Selector seguro de medios de Android", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }

                    // Option 2: Code File / Script
                    Surface(
                        onClick = {
                            showAttachmentPickerMenu = false
                            attachments = attachments + AttachmentItem(
                                id = UUID.randomUUID().toString(),
                                name = "contract_interface.kt",
                                type = AttachmentType.CODE,
                                sizeLabel = "14.2 KB"
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = GlassCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmpireGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = EmpireGold)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Archivo de Código Fuente (.kt / .json)", color = TextPlatinum, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Para reorganización y auditoría con Tony", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }

                    // Option 3: Confidential Document / Dossier
                    Surface(
                        onClick = {
                            showAttachmentPickerMenu = false
                            attachments = attachments + AttachmentItem(
                                id = UUID.randomUUID().toString(),
                                name = "dossier_financiero_miami.pdf",
                                type = AttachmentType.DOCUMENT,
                                sizeLabel = "2.8 MB"
                            )
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = GlassCard,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCrimson.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = NeonCrimson)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("Documento Confidencial (.pdf)", color = TextPlatinum, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("Inspección de inteligencia y metadatos", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showAttachmentPickerMenu = false }) {
                    Text("Cerrar", color = TextMuted)
                }
            }
        )
    }
}
