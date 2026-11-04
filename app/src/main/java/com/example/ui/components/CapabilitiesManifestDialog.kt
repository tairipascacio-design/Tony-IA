package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CapabilitiesManifestDialog(
    onDismiss: () -> Unit
) {
    val capabilities = listOf(
        CapabilityItem(
            icon = Icons.Default.ScreenShare,
            title = "TRANSMISIÓN DE PANTALLA EN VIVO",
            color = NeonCrimson,
            description = "La IA recibe un flujo continuo de frames a 1080p @ 60 FPS con telemetría en tiempo real para análisis visual contextual de lo que estás viendo en tu dispositivo."
        ),
        CapabilityItem(
            icon = Icons.Default.Speed,
            title = "AUTO-REORGANIZACIÓN Y REFACTOR DE CÓDIGO",
            color = NeonEmerald,
            description = "Capacidad autónoma de reescribir pipelines de render, podar recomposiciones innecesarias en Compose y generar un registro con diffs y métricas de mejora."
        ),
        CapabilityItem(
            icon = Icons.Default.RecordVoiceOver,
            title = "SÍNTESIS DE VOZ TONY MONTANA",
            color = EmpireGold,
            description = "Motor vocal adaptado con tono profundo (pitch 0.85), cadencia agresiva y jerga clásica de Scarface ('The world is yours', 'Say hello to my little friend')."
        ),
        CapabilityItem(
            icon = Icons.Default.CloudQueue,
            title = "ENLACE DIGITAL OCEAN",
            color = NeonCyan,
            description = "Conexión directa vía HTTP a clusters y droplets en Digital Ocean para inferencia con modelos como LLaMA 3.3 70B y Mistral Nemo sin límites de cuota."
        ),
        CapabilityItem(
            icon = Icons.Default.AttachFile,
            title = "INGESTA MULTI-ARCHIVO FORENSE",
            color = NeonPurple,
            description = "Soporte para recibir fotos, videos, scripts y documentos PDF, analizando metadatos e inyectándolos en el contexto del modelo."
        ),
        CapabilityItem(
            icon = Icons.Default.Extension,
            title = "ARSENAL DINÁMICO DE HERRAMIENTAS",
            color = Color(0xFFFF9E00),
            description = "Panel ampliable donde la IA y el usuario pueden registrar y orquestar herramientas especializadas con propósitos y capacidades explícitas."
        )
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ObsidianDarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = EmpireGold,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MANIFIESTO DE PODER DE LA IA",
                    color = EmpireGold,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 420.dp)
            ) {
                Text(
                    text = "Este es el conjunto de directivas y capacidades inyectadas en el núcleo de Tony Montana AI:",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(capabilities.size) { index ->
                        val item = capabilities[index]
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .glassmorphism(
                                    shape = RoundedCornerShape(10.dp),
                                    backgroundColor = Color(0x99101624),
                                    borderColor = item.color.copy(alpha = 0.4f)
                                )
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = item.color,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = item.title,
                                        color = item.color,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = item.description,
                                        color = TextPlatinum,
                                        fontSize = 10.sp,
                                        lineHeight = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmpireGold,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Entendido, Jefe", fontWeight = FontWeight.Black)
            }
        }
    )
}

data class CapabilityItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val color: Color,
    val description: String
)
