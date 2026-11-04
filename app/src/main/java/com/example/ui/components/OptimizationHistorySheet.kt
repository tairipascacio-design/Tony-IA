package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OptimizationRecord
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimizationHistorySheet(
    history: List<OptimizationRecord>,
    onTriggerOptimization: () -> Unit,
    onDismiss: () -> Unit
) {
    var isOptimizing by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = ObsidianDarkSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(44.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(NeonEmerald.copy(alpha = 0.6f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NeonEmerald,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AUTO-REORGANIZACIÓN DE CÓDIGO",
                            color = NeonEmerald,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = "Historial de parches y optimizaciones autónomas de la IA",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = {
                        isOptimizing = true
                        onTriggerOptimization()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonEmerald,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("trigger_self_optimization_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Optimizar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metrics Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricSummaryCard(
                    title = "RENDER",
                    value = "60 FPS",
                    sub = "0 frame drops",
                    color = NeonEmerald,
                    modifier = Modifier.weight(1f)
                )
                MetricSummaryCard(
                    title = "MEMORIA",
                    value = "98 MB",
                    sub = "Pool reciclado",
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricSummaryCard(
                    title = "ESTADO",
                    value = "REACTIVO",
                    sub = "Sin bloqueos",
                    color = EmpireGold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "REGISTRO DE PARCHES APLICADOS:",
                color = TextPlatinum,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history, key = { it.id }) { record ->
                    OptimizationCard(record = record)
                }
            }
        }
    }
}

@Composable
fun MetricSummaryCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .glassmorphism(
                shape = RoundedCornerShape(10.dp),
                backgroundColor = GlassCard,
                borderColor = color.copy(alpha = 0.5f)
            )
            .padding(10.dp)
    ) {
        Column {
            Text(
                text = title,
                color = color,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = value,
                color = TextPlatinum,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = sub,
                color = TextMuted,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
fun OptimizationCard(record: OptimizationRecord) {
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss • dd MMM", Locale.getDefault()) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphism(
                shape = RoundedCornerShape(14.dp),
                backgroundColor = Color(0xCC0D1420),
                borderColor = NeonEmerald.copy(alpha = 0.4f)
            )
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = record.module,
                color = NeonCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NeonEmerald.copy(alpha = 0.2f))
                    .border(1.dp, NeonEmerald.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = record.percentageGain,
                    color = NeonEmerald,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = record.title,
            color = TextPlatinum,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Before & After comparison
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF070B10))
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ANTES: ",
                    color = NeonCrimson,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = record.metricBefore,
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "AHORA: ",
                    color = NeonEmerald,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = record.metricAfter,
                    color = TextPlatinum,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Diff Snippet Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF05070A))
                .border(1.dp, Color(0xFF1E2633), RoundedCornerShape(8.dp))
                .horizontalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Text(
                text = record.diffSnippet,
                color = TextPlatinum,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                lineHeight = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = record.explanation,
            color = TextMuted,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Registrado a las ${dateFormat.format(Date(record.timestamp))}",
            color = TextDark,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}
