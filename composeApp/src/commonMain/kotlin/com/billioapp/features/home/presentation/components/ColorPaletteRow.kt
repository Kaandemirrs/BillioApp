package com.billioapp.features.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.CircleShape

@Composable
fun ColorPaletteRow(
    selectedColor: String?,
    onColorSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<String> = defaultPalette
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(colors) { hex ->
            val isSelected = selectedColor?.equals(hex, ignoreCase = true) == true
            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onColorSelected(hex) },
                color = hexToColor(hex),
                shape = CircleShape,
                border = if (isSelected) BorderStroke(3.dp, Color.White) else null,
            ) {}
        }
    }
}

private val defaultPalette = listOf(
    "#F44336", // Red
    "#E91E63", // Pink
    "#9C27B0", // Purple
    "#3F51B5", // Indigo
    "#2196F3", // Blue
    "#03A9F4", // Light Blue
    "#00BCD4", // Cyan
    "#4CAF50", // Green
    "#8BC34A", // Light Green
    "#CDDC39", // Lime
    "#FFEB3B", // Yellow
    "#FFC107", // Amber
    "#FF9800", // Orange
    "#FF5722", // Deep Orange
    "#795548", // Brown
    "#607D8B"  // Blue Grey
)

private fun hexToColor(hex: String): Color {
    val clean = hex.removePrefix("#")
    val value = clean.toLong(16)
    return when (clean.length) {
        6 -> Color(0xFF000000 or value)
        8 -> Color(value)
        else -> Color(0xFF9E9E9E)
    }
}