package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import com.example.ui.theme.*

@Composable
fun MontanaHeader(
    currentQuote: String,
    onQuoteClick: () -> Unit,
    isStreaming: Boolean,
    onToggleStream: () -> Unit,
    isSpeaking: Boolean,
    activeToolsCount: Int,
    onOpenToolsPanel: () -> Unit,
    onOpenOptimizationSheet: () -> Unit,
    onOpenDrawer: () -> Unit,
    modelName: String,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val livePulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ObsidianBlack,
                        ObsidianBlack.copy(alpha = 0.95f),
                        ObsidianDarkSurface.copy(alpha = 0.8f)
                    )
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Top Executive Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Drawer menu & Tony Identity
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onOpenDrawer,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("open_drawer_button")
                        .clip(RoundedCornerShape(12.dp))
                        .background(GlassCard)
                        .border(1.dp, GlassBorderGold, RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu Operaciones",
                        tint = EmpireGold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Tony Avatar with status ring
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            brush = Brush.sweepGradient(
                                listOf(EmpireGold, NeonCrimson, NeonCyan, EmpireGold)
                            ),
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.montana_avatar_1793582896308),
                        contentDescription = "Tony Montana Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "TONY MONTANA",
                            color = EmpireGold,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Voice speaking audio indicator
                        if (isSpeaking) {
                            VoiceWaveAnimation()
                        }
                    }
                    Text(
                        text = "DO: $modelName",
                        color = NeonCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Right Action Controls: Screen Stream HUD, Tools, Self-Optimization
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Live Screen Cast Trigger
                Surface(
                    onClick = onToggleStream,
                    shape = RoundedCornerShape(12.dp),
                    color = if (isStreaming) NeonCrimson.copy(alpha = 0.25f) else GlassCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isStreaming) NeonCrimson.copy(alpha = livePulseAlpha) else GlassBorder
                    ),
                    modifier = Modifier.testTag("toggle_screen_stream_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (isStreaming) NeonCrimson else TextDark)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Default.ScreenShare,
                            contentDescription = "Pantalla en vivo",
                            tint = if (isStreaming) NeonCrimson else TextPlatinum,
                            modifier = Modifier.size(16.dp)
                        )
                        if (isStreaming) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "EN VIVO",
                                color = NeonCrimson,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // AI Tools Panel Trigger
                Surface(
                    onClick = onOpenToolsPanel,
                    shape = RoundedCornerShape(12.dp),
                    color = GlassCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.6f)),
                    modifier = Modifier.testTag("open_tools_panel_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = "Herramientas IA",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$activeToolsCount",
                            color = NeonCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Self-Optimization Trigger
                IconButton(
                    onClick = onOpenOptimizationSheet,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("open_optimization_button")
                        .clip(RoundedCornerShape(10.dp))
                        .background(GlassCard)
                        .border(1.dp, NeonEmerald.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Auto-Optimización de Código",
                        tint = NeonEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cyberpunk Quote Ticker Glass Pill
        Surface(
            onClick = onQuoteClick,
            shape = RoundedCornerShape(8.dp),
            color = Color(0x990F1622),
            border = androidx.compose.foundation.BorderStroke(1.dp, GlassBorderGold),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("quote_ticker_pill")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Frase Tony",
                    tint = EmpireGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentQuote,
                    color = TextPlatinum,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "CAMBIAR",
                    color = EmpireGoldDark,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VoiceWaveAnimation() {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 14f,
        animationSpec = infiniteRepeatable(tween(250, easing = LinearEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(320, easing = LinearEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(tween(200, easing = LinearEasing), RepeatMode.Reverse),
        label = "h3"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = Modifier.height(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(h1.dp)
                .background(NeonCrimson, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(h2.dp)
                .background(EmpireGold, RoundedCornerShape(1.dp))
        )
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(h3.dp)
                .background(NeonCyan, RoundedCornerShape(1.dp))
        )
    }
}
