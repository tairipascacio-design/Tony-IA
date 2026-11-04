package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiTool
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiToolsPanel(
    tools: List<AiTool>,
    onToggleTool: (String) -> Unit,
    onExecuteTool: (String) -> Unit,
    onAddNewTool: (name: String, purpose: String, capabilities: List<String>, category: String) -> Unit,
    onDismiss: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf("TODAS") }

    val categories = listOf("TODAS", "Visión", "Optimización", "Cómputo", "Audio", "Archivos")
    val filteredTools = if (selectedCategoryFilter == "TODAS") {
        tools
    } else {
        tools.filter { it.category.equals(selectedCategoryFilter, ignoreCase = true) }
    }

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
                    .background(EmpireGold.copy(alpha = 0.6f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
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
                            imageVector = Icons.Default.Extension,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ARSENAL DE HERRAMIENTAS IA",
                            color = EmpireGold,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = "Capacidades dinámicas que la IA activa según la tarea",
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmpireGold,
                        contentColor = ObsidianBlack
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("add_custom_tool_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Añadir",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Category Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategoryFilter == cat
                    Surface(
                        onClick = { selectedCategoryFilter = cat },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) EmpireGold else GlassCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) EmpireGold else GlassBorder
                        )
                    ) {
                        Text(
                            text = cat,
                            color = if (isSelected) ObsidianBlack else TextPlatinum,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tools List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTools, key = { it.id }) { tool ->
                    AiToolCard(
                        tool = tool,
                        onToggle = { onToggleTool(tool.id) },
                        onExecute = { onExecuteTool(tool.id) }
                    )
                }

                item {
                    // System Manifest Explainer Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .glassmorphism(
                                shape = RoundedCornerShape(12.dp),
                                backgroundColor = Color(0x66151E2B),
                                borderColor = NeonCyan.copy(alpha = 0.4f)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "¿CÓMO USA LA IA ESTE ARSENAL?",
                                    color = NeonCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tony Montana invoca estas herramientas automáticamente cuando solicitas escanear la pantalla, reorganizar el código de la UI, procesar documentos forenses o sintetizar su voz. Puedes activar, desactivar o añadir nuevas armas al arsenal en cualquier momento.",
                                color = TextMuted,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddNewToolDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, purpose, capabilities, category ->
                onAddNewTool(name, purpose, capabilities, category)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AiToolCard(
    tool: AiTool,
    onToggle: () -> Unit,
    onExecute: () -> Unit
) {
    val categoryColor = when (tool.category.lowercase()) {
        "visión" -> NeonCrimson
        "optimización" -> NeonEmerald
        "cómputo" -> NeonCyan
        "audio" -> EmpireGold
        else -> NeonPurple
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .glassmorphism(
                shape = RoundedCornerShape(14.dp),
                backgroundColor = if (tool.isEnabled) GlassCard else Color(0x40101520),
                borderColor = if (tool.isEnabled) categoryColor.copy(alpha = 0.5f) else Color(0x33334155)
            )
            .padding(14.dp)
            .testTag("tool_card_${tool.id}")
    ) {
        // Top row: Category, Name, Enabled Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Category Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(categoryColor.copy(alpha = 0.2f))
                        .border(1.dp, categoryColor.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = tool.category.uppercase(),
                        color = categoryColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = tool.name,
                    color = TextPlatinum,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Switch(
                checked = tool.isEnabled,
                onCheckedChange = { onToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ObsidianBlack,
                    checkedTrackColor = categoryColor,
                    uncheckedThumbColor = TextDark,
                    uncheckedTrackColor = ObsidianCard
                ),
                modifier = Modifier.testTag("switch_${tool.id}")
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Tagline
        Text(
            text = tool.tagline,
            color = EmpireGold,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Purpose section
        Text(
            text = "PROPÓSITO:",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Text(
            text = tool.purpose,
            color = TextPlatinum.copy(alpha = 0.9f),
            fontSize = 11.sp,
            lineHeight = 15.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Capabilities chips
        Text(
            text = "CAPACIDADES REGISTRADAS:",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            tool.capabilities.forEach { cap ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0x3300F0FF))
                        .border(1.dp, Color(0x4400F0FF), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "• $cap",
                        color = NeonCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom stats & execute button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Ejecutado: ${tool.executionCount} veces",
                color = TextDark,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )

            Button(
                onClick = onExecute,
                enabled = tool.isEnabled && !tool.isExecuting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ObsidianCardElevated,
                    contentColor = TextPlatinum
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier
                    .border(1.dp, categoryColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .testTag("execute_${tool.id}")
            ) {
                if (tool.isExecuting) {
                    CircularProgressIndicator(
                        color = categoryColor,
                        modifier = Modifier.size(12.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ejecutando...", fontSize = 11.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = categoryColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Probar Herramienta", fontSize = 11.sp, color = categoryColor)
                }
            }
        }
    }
}

@Composable
fun AddNewToolDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, purpose: String, capabilities: List<String>, category: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("") }
    var capabilityInput by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Cómputo") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ObsidianDarkSurface,
        title = {
            Text(
                text = "AÑADIR HERRAMIENTA AL ARSENAL",
                color = EmpireGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre de la Herramienta") },
                    placeholder = { Text("ej. CyberProxyHunter") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = EmpireGold,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = { Text("Propósito para la IA") },
                    placeholder = { Text("¿Para qué usará Tony esta herramienta?") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = EmpireGold,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = capabilityInput,
                    onValueChange = { capabilityInput = it },
                    label = { Text("Capacidades (separadas por coma)") },
                    placeholder = { Text("ej. Scraping, Inyección, Bypass") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = EmpireGold,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && purpose.isNotBlank()) {
                        val caps = capabilityInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                        onConfirm(name, purpose, if (caps.isEmpty()) listOf("Capacidad autónoma") else caps, category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmpireGold, contentColor = ObsidianBlack)
            ) {
                Text("Montar Herramienta", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextMuted)
            }
        }
    )
}
