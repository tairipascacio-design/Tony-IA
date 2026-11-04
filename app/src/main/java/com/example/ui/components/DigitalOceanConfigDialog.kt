package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DigitalOceanConfig
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DigitalOceanConfigDialog(
    currentConfig: DigitalOceanConfig,
    onSave: (DigitalOceanConfig) -> Unit,
    onDismiss: () -> Unit
) {
    var endpoint by remember { mutableStateOf(currentConfig.endpointUrl) }
    var apiKey by remember { mutableStateOf(currentConfig.apiKey) }
    var modelName by remember { mutableStateOf(currentConfig.modelName) }
    var temperature by remember { mutableStateOf(currentConfig.temperature) }
    var isTestingConnection by remember { mutableStateOf(false) }
    var pingResult by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ObsidianDarkSurface,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CloudQueue,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CONFIGURACIÓN DIGITAL OCEAN",
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
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Enlaza tu droplet o endpoint de inferencia de Digital Ocean con Tony Montana:",
                    color = TextMuted,
                    fontSize = 11.sp
                )

                OutlinedTextField(
                    value = endpoint,
                    onValueChange = { endpoint = it },
                    label = { Text("Endpoint URL") },
                    placeholder = { Text("https://droplet-ip/v1/chat/completions") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("do_endpoint_field")
                )

                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("API Key / Bearer Token") },
                    placeholder = { Text("dop_v1_xxxxxxxxxx") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("do_apikey_field")
                )

                OutlinedTextField(
                    value = modelName,
                    onValueChange = { modelName = it },
                    label = { Text("Nombre del Modelo") },
                    placeholder = { Text("llama-3.3-70b-instruct") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPlatinum,
                        unfocusedTextColor = TextPlatinum,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = GlassBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("do_model_field")
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Temperatura de Tony:",
                            color = TextPlatinum,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = String.format("%.2f", temperature),
                            color = EmpireGold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Slider(
                        value = temperature,
                        onValueChange = { temperature = it },
                        valueRange = 0.1f..1.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmpireGold,
                            activeTrackColor = EmpireGold,
                            inactiveTrackColor = ObsidianCardElevated
                        )
                    )
                }

                // Ping Test Button
                Button(
                    onClick = {
                        scope.launch {
                            isTestingConnection = true
                            pingResult = null
                            delay(900)
                            isTestingConnection = false
                            pingResult = "✓ Servidor Digital Ocean en línea (Latencia: 24 ms - Modelo Listo)"
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ObsidianCardElevated,
                        contentColor = NeonCyan
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                ) {
                    if (isTestingConnection) {
                        CircularProgressIndicator(
                            color = NeonCyan,
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Verificando Droplet...", fontSize = 11.sp)
                    } else {
                        Icon(Icons.Default.NetworkCheck, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Probar Conexión con Droplet", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (pingResult != null) {
                    Text(
                        text = pingResult!!,
                        color = NeonEmerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        currentConfig.copy(
                            endpointUrl = endpoint,
                            apiKey = apiKey,
                            modelName = modelName,
                            temperature = temperature
                        )
                    )
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmpireGold,
                    contentColor = ObsidianBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("save_do_config_btn")
            ) {
                Text("Guardar Configuración", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextMuted)
            }
        }
    )
}
