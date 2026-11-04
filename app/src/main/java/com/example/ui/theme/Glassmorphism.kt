package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.glassmorphism(
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = GlassCard,
    borderColor: Color = GlassBorder,
    borderWidth: Dp = 1.dp
): Modifier = this
    .clip(shape)
    .background(
        brush = Brush.verticalGradient(
            colors = listOf(
                backgroundColor.copy(alpha = 0.85f),
                backgroundColor.copy(alpha = 0.65f)
            )
        ),
        shape = shape
    )
    .border(
        width = borderWidth,
        brush = Brush.linearGradient(
            colors = listOf(
                borderColor.copy(alpha = 0.8f),
                borderColor.copy(alpha = 0.2f),
                borderColor.copy(alpha = 0.6f)
            )
        ),
        shape = shape
    )

fun Modifier.goldGlass(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = glassmorphism(
    shape = shape,
    backgroundColor = Color(0xCC1A1608),
    borderColor = EmpireGold.copy(alpha = 0.5f),
    borderWidth = 1.dp
)

fun Modifier.crimsonGlass(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = glassmorphism(
    shape = shape,
    backgroundColor = Color(0xCC1E0A12),
    borderColor = NeonCrimson.copy(alpha = 0.6f),
    borderWidth = 1.dp
)

fun Modifier.cyanGlass(
    shape: Shape = RoundedCornerShape(16.dp)
): Modifier = glassmorphism(
    shape = shape,
    backgroundColor = Color(0xCC091822),
    borderColor = NeonCyan.copy(alpha = 0.6f),
    borderWidth = 1.dp
)
