package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScreenStreamState
import com.example.ui.theme.*

@Composable
fun ScreenShareHud(
    streamState: ScreenStreamState,
    onToggleStream: () -> Unit,
    onCaptureAndAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .glassmorphism(
                shape = RoundedCornerShape(16.dp),
                backgroundColor = if (streamState.isStreaming) Color(0xDD120A14) else Color(0xCC0D121B),
                borderColor = if (streamState.isStreaming) NeonCrimson.copy(alpha = 0.8f) else GlassBorder
            )
            .padding(12.dp)
    ) {
        // HUD Header Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (streamState.isStreaming) StatusLive else TextDark)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (streamState.isStreaming) "STREAMING EN VIVO A TONY MONTANA" else "TRANSMISIÓN DE PANTALLA EN PAUSA",
                    color = if (streamState.isStreaming) NeonCrimson else TextPlatinum,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }

            IconButton(
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Colapsar HUD",
                    tint = TextMuted
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Viewfinder Screen Canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF06090E))
                        .border(1.dp, if (streamState.isStreaming) NeonCrimson.copy(alpha = 0.5f) else Color(0xFF1E2838), RoundedCornerShape(10.dp))
                ) {
                    CyberpunkRadarCanvas(isStreaming = streamState.isStreaming)

                    // Overlay Telemetry
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "RADAR: ${streamState.resolution}",
                                color = NeonCyan,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "${streamState.fps} FPS • ${streamState.bitrateMbps} Mbps",
                                color = NeonEmerald,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Crosshair Center Target
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .glassmorphism(
                                    shape = RoundedCornerShape(8.dp),
                                    backgroundColor = Color(0x80000000),
                                    borderColor = EmpireGold.copy(alpha = 0.6f)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = if (streamState.isStreaming) "DETECTANDO ACTIVIDAD EN PANTALLA" else "TRANSMISIÓN DESCONECTADA",
                                color = EmpireGold,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "LATENCIA: ${streamState.latencyMs} ms",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "FRAMES: ${streamState.framesAnalyzed}",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tony Montana's live vision commentary
                Text(
                    text = streamState.lastVisionAnalysis,
                    color = TextPlatinum,
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // HUD Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onToggleStream,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (streamState.isStreaming) NeonCrimson else ObsidianCardElevated,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stream_toggle_action_btn")
                    ) {
                        Icon(
                            imageVector = if (streamState.isStreaming) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (streamState.isStreaming) "Pausar Stream" else "Iniciar Stream",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onCaptureAndAnalyze,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmpireGold,
                            contentColor = ObsidianBlack
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("analyze_screen_with_tony_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Analizar con Tony",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CyberpunkRadarCanvas(isStreaming: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "radar")
    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(4000, easing = LinearEasing), RepeatMode.Restart),
        label = "sweep"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Draw gridlines
        val gridStep = 24.dp.toPx()
        var x = 0f
        while (x < width) {
            drawLine(
                color = Color(0x1500F0FF),
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += gridStep
        }

        var y = 0f
        while (y < height) {
            drawLine(
                color = Color(0x1500F0FF),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += gridStep
        }

        if (isStreaming) {
            // Draw holographic scanning radar lines
            val center = Offset(width / 2f, height / 2f)
            val radius = minOf(width, height) / 2.2f

            drawCircle(
                color = Color(0x22FF2A6D),
                radius = radius,
                center = center,
                style = Stroke(width = 1.5f)
            )
            drawCircle(
                color = Color(0x18FF2A6D),
                radius = radius * 0.6f,
                center = center,
                style = Stroke(width = 1.5f)
            )

            // Scanning sweep line
            val rad = Math.toRadians(sweepAngle.toDouble())
            val targetX = center.x + radius * Math.cos(rad).toFloat()
            val targetY = center.y + radius * Math.sin(rad).toFloat()
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0x00FF2A6D), Color(0xAAFF2A6D)),
                    start = center,
                    end = Offset(targetX, targetY)
                ),
                start = center,
                end = Offset(targetX, targetY),
                strokeWidth = 2f
            )
        }
    }
}
